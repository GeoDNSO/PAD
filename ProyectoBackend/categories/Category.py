import constants as c
from bson.objectid import ObjectId

class Category:
    def __init__(self,id="-1", name="", creation_time="", json=None):
        if(json is None):
            self.id = id
            self.name = name
            self.creation_time = creation_time
        else:
            self.initialize(json)

    def initialize(self, json):
        self.id = json[c.DB_ID_KEY] if c.DB_ID_KEY in json else "-1"
        self.name = json[c.DB_CATEGORY_NAME_KEY]
        self.creation_time = json[c.DB_CREATION_TIME]

    def to_dict(self, id_in=True):
        if id_in:
            return {
                c.DB_ID_KEY: str(self.id), #ObjectID to str
                c.DB_CATEGORY_NAME_KEY: self.name,
                c.DB_CREATION_TIME: self.creation_time
            }
        return {
            c.DB_CATEGORY_NAME_KEY: self.name,
            c.DB_CREATION_TIME: self.creation_time
        }
