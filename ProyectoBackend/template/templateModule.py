from os import path
from re import A

from pymongo.message import delete
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
from util.utilities import time_now_str
from flask import current_app as app

import constants
from bson.objectid import ObjectId

from pprint import pprint

from .TemplateModuleHelper import convertB64Images, deleteDirectory

templateModule = Blueprint("templateModule", __name__)

# Custom static data, to send Files Images
@templateModule.route('/_templateImages/<path:filename>')
def custom_static(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

@templateModule.route('/deleteTemplate/', methods=['DELETE'])
def deleteTemplate():
    json_data = request.get_json()
    
    idToDelete = ObjectId(json_data[constants.DB_ID_KEY])

    try:
        mongo.db.templates.delete_one({constants.DB_ID_KEY: idToDelete}) #Borrar template
        mongo.db.tiers_done.delete_many({constants.DB_TEMPLATE_ID: idToDelete}) #Borrar tiers realizados con el template a borrar
        deleteDirectory(str(idToDelete))

        response = jsonify({constants.DB_RESULT: "Template deleted succesfully"})
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error deleting template"})
        response.status_code = 404
        return response

@templateModule.route('/createTemplate/', methods=['POST'])
def createTemplate():
    json_data = request.get_json()
    
    template = Template(json=json_data)
    template.creation_time = time_now_str()

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

def getPageAndLimit(args):
    page = 1
    limit = 1
    try:
        page = int(args[constants.PAGINATION_PAGE])
        limit = int(args[constants.PAGINATION_LIMIT])
    except:
        page = 1
        limit = 1
    return page, limit

@templateModule.route('/listTemplates/', methods=['GET'])
def listTemplates():

    page, limit = getPageAndLimit(request.args)
    
    custom_args = request.args.copy()
    custom_args.pop(constants.PAGINATION_PAGE)
    custom_args.pop(constants.PAGINATION_LIMIT)
    
    try:
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

'''
db.collection.aggregate([{
  '$group': {
    '_id': {'author': '$author', 'subreddit': '$subreddit'}, 
    'count': {'$sum': 1}, 
    'author': {'$last': '$author'}}
}, {
  '$match': {
    'count': {'$eq': 1}
}}])'''

'''
group_option = {"$group": 
                        {"_id": {f"${constants.DB_TEMPLATE_ID}"}, 
                        "count": {"$sum": f"${constants.DB_TEMPLATE_ID}"}}
                        }
'''

@templateModule.route('/listPopularTemplates/', methods=['GET'])
def listPopularTemplates():

    page, limit = getPageAndLimit(request.args)
    
    try:
        group_option = {"$group": 
                            {
                            "_id": f"${constants.DB_TEMPLATE_ID}", 
                            "count": {"$sum": 1}
                            }
                        }

        sort = {"$sort": {"count":-1}}

        cursor = mongo.db.tiers_done.aggregate([group_option, sort])
        listCount = templateUtils.tierListInfoFromCursor(cursor)
        print(listCount)
        c_filter = {"_id": {"$in": listCount}}
        cursor = mongo.db.templates.find(c_filter).skip((page-1)*limit).limit(limit)
    
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