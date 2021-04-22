import constants as c


class Template:
    def __init__(self, title="", category="", creator_username=-1, container=[], tier_rows={}, json=None):
        if(json is None):
            self.title = title
            self.category = category
            self.creator_username = creator_username
            self.container = container
            self.tier_rows = tier_rows
        else:
            self.initialize(json)

    def initialize(self, json):
        self.title = json[c.DB_TITLE_KEY]
        self.category = json[c.DB_CATEGORY_KEY]
        self.creator_username = json[c.DB_CREATOR_USERNAME_KEY]
        self.container = json[c.DB_CONTAINER_KEY]
        self.tier_rows = json[c.DB_TIER_ROWS_KEY]

    def to_dict(self):
        return {
            c.DB_TITLE_KEY: self.title,
            c.DB_CATEGORY_KEY: self.category,
            c.DB_CREATOR_USERNAME_KEY: self.creator_username,
            c.DB_CONTAINER_KEY: self.container,
            c.DB_TIER_ROWS_KEY: self.tier_rows
        }
