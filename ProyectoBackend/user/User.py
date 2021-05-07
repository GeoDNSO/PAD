import constants as c

class User:
  def __init__(self, username="", password="", email="", icon="", json=None):
    if(json is None):
      self.username = username
      self.password = password
      self.email = email
      self.icon = icon
    else:
      self.initialize(json)
    
  def initialize(self, json):
    self.username = json[c.DB_USERNAME_KEY]
    self.password = json[c.DB_PASSWORD_KEY]
    self.email = json[c.DB_EMAIL_KEY]
    self.icon = json[c.DB_ICON_KEY]

  def to_dict(self):
    return {c.DB_USERNAME_KEY: self.username,
            c.DB_PASSWORD_KEY: self.password,
            c.DB_EMAIL_KEY: self.email, 
            c.DB_ICON_KEY: self.icon}