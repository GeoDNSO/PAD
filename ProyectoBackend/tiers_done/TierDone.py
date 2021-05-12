import constants as c
from bson.objectid import ObjectId
from util.utilities import time_now_str

class TierDone:
    def __init__(self,tierdone_id=-1, template_id=-1, creator_username="", container=[], tier_rows={},creation_time="", json=None):
        if(json is None):
            self.tierdone_id = tierdone_id
            self.template_id = template_id
            self.creator_username = creator_username
            self.container = container
            self.tier_rows = tier_rows
            self.creation_time = creation_time
        else:
            self.initialize(json)

    def initialize(self, json):
        self.tierdone_id = json[c.DB_ID_KEY] if c.DB_ID_KEY in json else "-1"
        self.template_id = json[c.DB_TEMPLATE_ID]
        self.creator_username = json[c.DB_CREATOR_USERNAME_KEY]
        self.container = json[c.DB_CONTAINER_KEY]
        self.tier_rows = json[c.DB_TIER_ROWS_KEY]
        self.creation_time = json[c.DB_CREATION_TIME] if c.DB_CREATION_TIME in json else time_now_str

    def to_dict(self):
        return {
            c.DB_ID_KEY: str(self.tierdone_id),
            c.DB_TEMPLATE_ID: str(self.template_id), #ObjectId no es serializable
            c.DB_CREATOR_USERNAME_KEY: self.creator_username,
            c.DB_CONTAINER_KEY: self.container,
            c.DB_TIER_ROWS_KEY: self.tier_rows,
            c.DB_CREATION_TIME: self.creation_time
        }
