import constants as c
from bson.objectid import ObjectId

class TierDone:
    def __init__(self, template_id=-1, creator_username="", container=[], tier_rows={}, json=None):
        if(json is None):
            self.template_id = template_id
            self.creator_username = creator_username
            self.container = container
            self.tier_rows = tier_rows
        else:
            self.initialize(json)

    def initialize(self, json):
        self.template_id = json[c.DB_TEMPLATE_ID]
        self.creator_username = json[c.DB_CREATOR_USERNAME_KEY]
        self.container = json[c.DB_CONTAINER_KEY]
        self.tier_rows = json[c.DB_TIER_ROWS_KEY]

    def to_dict(self):
        return {
            c.DB_TEMPLATE_ID: str(self.template_id), #ObjectId no es serializable
            c.DB_CREATOR_USERNAME_KEY: self.creator_username,
            c.DB_CONTAINER_KEY: self.container,
            c.DB_TIER_ROWS_KEY: self.tier_rows
        }
