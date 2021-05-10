from flask import Blueprint
from flask import request
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from pymongo.message import query
from database import mongo

from util.utilities import time_now_str
from flask import current_app as app
from . import categoryUtils

import constants
from bson.objectid import ObjectId

from util.utilities import getCustomArgs, getPageAndLimit
import base64
import os, errno
import uuid
import shutil

def getPopularCategories(args):

    page, limit = getPageAndLimit(args)


    lookup = { "$lookup": 
                {
                    "from": "tiers_done",
                    "localField": "_id",
                    "foreignField": "template_id",
                    "as": "tiers"
                }
            }

    tiersSize = {"$size": "$tiers"}
    group_option = {"$group": 
                            {
                            "_id": f"${constants.DB_CATEGORY_KEY}", 
                            "category_count": {"$sum": tiersSize}
                            }
                    }


    project = {"$project": 
                {
                    "_id":0,
                    "category": "$_id",
                    "categorycount": "$category_count"

                }
            }


    sort = {"$sort": {"categorycount":-1}}

    limit_param = {"$limit":limit}
    skip_param = {"$skip":((page-1)*limit)}

    query = [lookup, group_option, project, sort, skip_param, limit_param]

    cursor = mongo.db.templates.aggregate(query)#.skip((page-1)*limit).limit(limit)
    listCount = categoryUtils.listFromPopularCursor(cursor)

    return listCount