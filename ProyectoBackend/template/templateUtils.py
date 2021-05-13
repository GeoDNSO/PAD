from flask import jsonify
from database import mongo

from .Template import Template

import constants

from bson.objectid import ObjectId

from pprint import pprint

#Return args without page and limit to make a custom search

def tierListInfoFromCursor(cursor):
    tierInfo = []
    for item in cursor:
        finalItem = ObjectId(item[constants.DB_ID_KEY])
        tierInfo.append(finalItem)
    return tierInfo

def listFromCursor(cursor):
    templateList = []
    for item in cursor:
        template = Template(json=item)
        templateList.append(template.to_dict())
    return templateList

def templateListCursor(mongo, args):
    cursor = mongo.db.templates.find(args)
    return cursor