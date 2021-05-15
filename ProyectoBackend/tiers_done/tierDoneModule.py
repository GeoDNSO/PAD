from template.templateUtils import listFromCursor
from flask import Blueprint
from flask import request
from flask import jsonify
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo
from bson.objectid import ObjectId
from util.utilities import time_now_str

from .TierDone import TierDone
from . import tiersDoneUtils

import constants

from pprint import pprint

tiersDoneModule = Blueprint("tiersDoneModule", __name__)

#{_id: ObjectId("60819b04a76afa43846700d5")}


@tiersDoneModule.route('/createTierDone/', methods=['POST'])
def createTierDone():
    json_data = request.get_json()

    tierDone = TierDone(json=json_data)
    tierDone.creation_time = time_now_str()
    tierDoneDict = tierDone.to_dict()
    tierDoneDict.pop(constants.DB_ID_KEY)#Para no usar un ID incorrecto creado por defecto

    try:
        set = {"$set": tierDoneDict}
        filter = {constants.DB_TEMPLATE_ID: json_data[constants.DB_TEMPLATE_ID], 
                constants.DB_CREATOR_USERNAME_KEY: json_data[constants.DB_CREATOR_USERNAME_KEY]}

        mongo.db.tiers_done.update_one(filter, set, upsert=True) #Crear o actualizar el tier si ya existia
        tierDoneJSON = mongo.db.tiers_done.find_one(filter) #Pedir el tier para devolverlo a la aplicacion

        tierDoneDict = TierDone(json=tierDoneJSON).to_dict()

        response = jsonify(tierDoneDict)
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al crear/actualizar un tier"})
        response.status_code = 400
        return response

@tiersDoneModule.route('/getTierDone/<id>', methods=['GET'])
def getTierDone(id):
    try:

        tierDoneJSON = mongo.db.tiers_done.find_one({constants.DB_ID_KEY: ObjectId(id)})
        tierDone = TierDone(json=tierDoneJSON)
        print(tierDone.to_dict())
       
        response = jsonify(tierDone.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al buscar el template"})
        response.status_code = 400
        return response

@tiersDoneModule.route('/getTierDone/', methods=['GET'])
def getTierDoneBy():
    args = request.args.to_dict()
    print(args)
    filter = {
        constants.DB_CREATOR_USERNAME_KEY: args[constants.DB_CREATOR_USERNAME_KEY], 
        constants.DB_TEMPLATE_ID: args[constants.DB_TEMPLATE_ID]
    }
    print("AHora filtro...")
    print(filter)
    
    try:
        tierDoneJSON = mongo.db.tiers_done.find_one(filter)
        tierDone = TierDone(json=tierDoneJSON)
        print(tierDone.to_dict())
       
        response = jsonify(tierDone.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al buscar el tier"})
        response.status_code = 400
        return response

@tiersDoneModule.route('/listTiersDone/', methods=['GET'])
def listTiersDone():

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
        cursor = mongo.db.tiers_done.find(custom_args).skip((page-1)*limit).limit(limit)
        templateList = tiersDoneUtils.listFromCursor(cursor)
       
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