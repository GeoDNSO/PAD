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


from pprint import pprint

templateModule = Blueprint("templateModule", __name__)

#Si fuese necsario otro tipo de filtro, usar metodo get con args y quitar request.get_json() para evitar error 400
@templateModule.route('/getTemplate/', methods=['POST'])
def getTemplate():
    json_data = request.get_json()

    templateTitle = json_data[constants.DB_TITLE_KEY]

    try:
        templateJSON = mongo.db.templates.find_one({constants.DB_TITLE_KEY: templateTitle})
        template = Template(json=templateJSON)
       
        response = jsonify(template.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al buscar el template"})
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
        return response