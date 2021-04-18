from flask import Blueprint
from flask import request
from flask import jsonify
from flask_pymongo import PyMongo
from pymongo import errors
from database import mongo

from werkzeug.security import generate_password_hash, check_password_hash

import constants

from pprint import pprint


userModule = Blueprint("userModule", __name__)


@userModule.route('/login/', methods=['GET'])
def login():
   return "not finished"

@userModule.route('/registerUser/', methods=['POST'])
def registerUser():
    json_data = request.get_json()
    username = json_data[constants.DB_USERNAME_KEY]
    password = json_data[constants.DB_PASSWORD_KEY]
    email = json_data[constants.DB_EMAIL_KEY]

    try:
        hashed_password = generate_password_hash(password)
        id = mongo.db.users.insert_one({'username': username, 'email': email, 'password': hashed_password})
        
        response = jsonify({
            '_id': str(id),
            'username': username,
            'password': password,
            'email': email
        })
        response.status_code = 201
        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "error"})
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