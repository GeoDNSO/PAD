from flask import jsonify
from database import mongo

from .Category import Category

import constants

from pprint import pprint

def listFromCursor(cursor):
    categoryList = []
    for item in cursor:
        category = Category(json=item)
        categoryList.append(category.to_dict())
    return categoryList
