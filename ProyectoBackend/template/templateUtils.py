from flask import jsonify
from database import mongo

from .Template import Template

import constants

from pprint import pprint

def listFromCursor(cursor):
    templateList = []
    for item in cursor:
        print(item)
        template = Template(json=item)
        templateList.append(template.to_dict())
    return templateList

def templateListCursor(mongo, args):
    cursor = mongo.db.templates.find(args)
    return cursor