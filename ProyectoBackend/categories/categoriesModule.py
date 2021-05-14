from categories.Category import Category
from os import path

from pymongo.message import delete
from template.templateUtils import listFromCursor
from flask import Blueprint
from flask import request
from flask import jsonify, send_from_directory
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo

from .Category import Category
from .categoryUtils import listFromCursor
from . import categoryHelper

from flask import current_app as app

import constants
from bson.objectid import ObjectId

from pprint import pprint
import yaml
from util.utilities import getCustomArgs, getPageAndLimit, time_now_str
import os
import io

categoriesModule = Blueprint("categoriesModule", __name__)

# Custom static data, to send Files Images
@categoriesModule.route('/uploadCategoriesToDatabase/', methods=['POST'])
def uploadCategoriesToDatabase():
    file = os.path.dirname(os.path.join(app.root_path , "categories", constants.CATEGORIES_YAML_FILE, ""))
    try:
        with open(file, 'r',  encoding="utf-8") as stream:
            out = yaml.load(stream, yaml.FullLoader)
            categoriesList = [Category("-1",name, time_now_str()).to_dict(False) for name in out['Categories']]
            mongo.db.categories.insert_many(categoriesList)

            response = jsonify({
                constants.DB_RESULT: "Categories uploaded to database successfully",
                constants.DB_CATEGORIES_LIST: categoriesList
            })
            response.status_code = 200 # OK

            return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error uploading categories"})
        response.status_code = 404
        return response


@categoriesModule.route('/getCategories/', methods=['GET'])
def getCategories():
    try:
      
        cursor = mongo.db.categories.find()
        categoriesList = listFromCursor(cursor)
        
        response = jsonify({
            constants.DB_CATEGORIES_LIST: categoriesList
        })
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error getting categories"})
        response.status_code = 404
        return response


@categoriesModule.route('/getPopularCategories/', methods=['GET'])
def getPopularCategories():
    try:
      
        categoriesList = categoryHelper.getPopularCategories(request.args)
        
        response = jsonify({
            constants.DB_CATEGORIES_LIST: categoriesList
        })
        response.status_code = 200 # OK

        return response
    except errors.PyMongoError as e:
        print("Error PyMongo: ", repr(e))
        response = jsonify({"error": "Error getting categories"})
        response.status_code = 404
        return response