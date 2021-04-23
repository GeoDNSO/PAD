from template.templateUtils import listFromCursor
from flask import Blueprint
from flask import request
from flask import jsonify
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo

from .Template import Template
#from .templateUtils import *
from . import templateUtils

import constants
from bson.objectid import ObjectId

from pprint import pprint

templateModule = Blueprint("templateModule", __name__)

@templateModule.route('/createTemplate/', methods=['POST'])
def createTemplate():
    json_data = request.get_json()
    
    template = Template(json=json_data)

    try:
        templateDict = template.to_dict()
        templateDict.pop(constants.DB_TEMPLATE_ID)#Para no usar un ID incorrecto creado por defecto

        id = mongo.db.templates.insert_one(templateDict).inserted_id

        
        templateDict[constants.DB_TEMPLATE_ID] = str(id) #Devolvemos el id correcto
       
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

    custom_args = request.args.copy()
    custom_args.pop(constants.PAGINATION_PAGE)
    custom_args.pop(constants.PAGINATION_LIMIT)

    try:
        #cursor = templateUtils.templateListCursor(mongo=mongo, args=request.args)
        cursor = mongo.db.templates.find(custom_args).skip((page-1)*limit).limit(limit)
        templateList = templateUtils.listFromCursor(cursor)
       
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