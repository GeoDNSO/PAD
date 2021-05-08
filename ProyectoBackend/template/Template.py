import constants as c
from bson.objectid import ObjectId
from util.utilities import time_now_str

class Template:
    def __init__(self,id="-1", title="", cover="", category="", creator_username=-1, container=[], tier_rows={}, creation_time="", json=None):
        if(json is None):
            self.id = id
            self.title = title
            self.cover = cover
            self.category = category
            self.creator_username = creator_username
            self.container = container
            self.tier_rows = tier_rows
            self.creation_time = creation_time
        else:
            self.initialize(json)

    def initialize(self, json):
        self.id = json[c.DB_ID_KEY] if c.DB_ID_KEY in json else "-1"
        self.title = json[c.DB_TITLE_KEY]
        self.cover = json[c.DB_COVER_KEY] if c.DB_COVER_KEY in json else "no_cover"
        self.category = json[c.DB_CATEGORY_KEY]
        self.creator_username = json[c.DB_CREATOR_USERNAME_KEY]
        self.container = json[c.DB_CONTAINER_KEY]
        self.tier_rows = json[c.DB_TIER_ROWS_KEY]
        self.creation_time = json[c.DB_CREATION_TIME] if c.DB_CREATION_TIME in json else time_now_str()

    def to_dict(self):
        return {
            c.DB_ID_KEY: str(self.id), #ObjectID to str
            c.DB_TITLE_KEY: self.title,
            c.DB_COVER_KEY: self.cover,
            c.DB_CATEGORY_KEY: self.category,
            c.DB_CREATOR_USERNAME_KEY: self.creator_username,
            c.DB_CONTAINER_KEY: self.container,
            c.DB_TIER_ROWS_KEY: self.tier_rows,
            c.DB_CREATION_TIME: self.creation_time
        }
