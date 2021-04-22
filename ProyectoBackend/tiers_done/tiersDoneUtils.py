from flask import jsonify
from database import mongo

from .TierDone import TierDone

import constants

from pprint import pprint

def listFromCursor(cursor):
    tiersDoneList = []
    for item in cursor:
        print(item)
        template = TierDone(json=item)
        tiersDoneList.append(template.to_dict())
    return tiersDoneList
