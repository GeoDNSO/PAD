from flask import Blueprint
from flask import request
from flask import jsonify
from flask_pymongo import PyMongo
from pymongo import errors
from database import mongo

from .User import User

from werkzeug.security import generate_password_hash, check_password_hash

import constants

from pprint import pprint

userModule = Blueprint("userModule", __name__)

@userModule.route('/login/', methods=['POST'])
def login():
    json_data = request.get_json()
    username = json_data[constants.DB_USERNAME_KEY]
    password = json_data[constants.DB_PASSWORD_KEY]

    givenUser = User(username, password, "")

    try:
        userToReturnJSON = mongo.db.users.find_one({constants.DB_USERNAME_KEY: givenUser.username})
        userToReturn = User(json=userToReturnJSON)

        if(check_password_hash(userToReturn.password, givenUser.password) == False):
            response = jsonify({
                "error": "Creadenciales incorrectas",
                "givenUser": givenUser.to_dict()
            })
            response.status_code = 401 # 401 = Unauthorized
            return response
       
        response = jsonify(userToReturn.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al hacer login del usuario"})
        return response

@userModule.route('/registerUser/', methods=['POST'])
def registerUser():
    json_data = request.get_json()
    username = json_data[constants.DB_USERNAME_KEY]
    password = json_data[constants.DB_PASSWORD_KEY]
    email = json_data[constants.DB_EMAIL_KEY]

    hashed_password = generate_password_hash(password)

    user = User(username, hashed_password, email)

    try:
        id = mongo.db.users.insert_one(user.to_dict()).inserted_id

        userDict = user.to_dict()
        userDict['id'] = str(id)

        response = jsonify(userDict)
        response.status_code = 201 # 201 = Creado con éxito

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al crear usuario"})
        return response
      
@userModule.route('/getUser/', methods=['POST'])
def getUser():
    json_data = request.get_json()
    username = json_data[constants.DB_USERNAME_KEY]

    try:
        userToReturnJSON = mongo.db.users.find_one({constants.DB_USERNAME_KEY: username})
        userToReturn = User(json=userToReturnJSON)
        userToReturn.password = "" # No pasamos la password del usuario
       
        response = jsonify(userToReturn.to_dict())
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error al hacer login del usuario"})
        return response

@userModule.errorhandler(404)
def not_found(error=None):
    message = {
        'message': 'Resource Not Found ' + request.url,
        'status': 404
    }
    response = jsonify(message)
    response.status_code = 404
    return response
