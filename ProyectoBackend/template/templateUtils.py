from flask import jsonify
from database import mongo

from .Template import Template

import constants

from pprint import pprint

#Return args without page and limit to make a custom search
def getCustomArgs(args):
    custom_args = args.copy()
    custom_args.pop(constants.PAGINATION_PAGE)
    custom_args.pop(constants.PAGINATION_LIMIT)
    return custom_args

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

def tierListInfoFromCursor(cursor):
    tierInfo = []
    for item in cursor:
        tierInfo.append(item[constants.DB_ID_KEY])
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