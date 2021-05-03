from os import path
from template.templateUtils import listFromCursor
from flask import Blueprint
from flask import request
from flask import jsonify, send_from_directory
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo

from .Template import Template
#from .templateUtils import *
from . import templateUtils

from flask import current_app as app

import constants
from bson.objectid import ObjectId

from pprint import pprint

from .TemplateModuleHelper import convertB64Images

templateModule = Blueprint("templateModule", __name__)

# Custom static data, to send Files Images
@templateModule.route('/_templateImages/<path:filename>')
def custom_static(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

@templateModule.route('/createTemplate/', methods=['POST'])
def createTemplate():
    json_data = request.get_json()
    
    template = Template(json=json_data)

    try:
        templateDict = template.to_dict()
        templateDict.pop(constants.DB_ID_KEY)#Para no usar un ID incorrecto creado por defecto

        id = mongo.db.templates.insert_one(templateDict).inserted_id

        templateDict[constants.DB_ID_KEY] = str(id) #Devolvemos el id correcto

        convertB64Images(templateDict)

        #Actualizar los valores del template en BD para guardar el path a la imagen
        selected = {constants.DB_ID_KEY: id}
        updated_values = {"$set": {constants.DB_COVER_KEY: templateDict[constants.DB_COVER_KEY], constants.DB_CONTAINER_KEY: templateDict[constants.DB_CONTAINER_KEY]}}
        mongo.db.templates.update_one(selected, updated_values)

        response = jsonify(templateDict)
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al crear el template"})
        response.status_code = 400
        return response


@templateModule.route('/getTemplate/<id>', methods=['GET'])
def getTemplate(id):
    
    try:
        templateJSON = mongo.db.templates.find_one({constants.DB_ID_KEY: ObjectId(id)})
        template = Template(json=templateJSON)
       
        response = jsonify(template.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al buscar el template"})
        response.status_code = 400
        return response

@templateModule.route('/listTemplates/', methods=['GET'])
def listTemplates():

    page = 1
    limit = 1
    try:
        page = int(request.args[constants.PAGINATION_PAGE])
        limit = int(request.args[constants.PAGINATION_LIMIT])
    except:
        page = 1
        limit = 1
    print("Args: ", request.args)
    custom_args = request.args.copy()
    custom_args.pop(constants.PAGINATION_PAGE)
    custom_args.pop(constants.PAGINATION_LIMIT)
    print("Custom args: ", custom_args)
    try:
        #cursor = templateUtils.templateListCursor(mongo=mongo, args=request.args)
        cursor = mongo.db.templates.find(custom_args).skip((page-1)*limit).limit(limit)
        templateList = templateUtils.listFromCursor(cursor)
        print("Lista")
        print(templateList)
       
        response = jsonify({
            "elements": len(templateList),
            "list": templateList})
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al buscar la lista de templates"})
        response.status_code = 400
        return response