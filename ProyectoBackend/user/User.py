import constants as c
from util.utilities import time_now_str

class User:
  def __init__(self, username="", password="", email="", icon="",creation_time="",rol="", json=None):
    if(json is None):
      self.username = username
      self.password = password
      self.email = email
      self.icon = icon
      self.creation_time = creation_time
      self.rol = rol
    else:
      self.initialize(json)
    
  def initialize(self, json):
    self.username = json[c.DB_USERNAME_KEY]
    self.password = json[c.DB_PASSWORD_KEY]
    self.email = json[c.DB_EMAIL_KEY]
    self.icon = json[c.DB_ICON_KEY]
    self.creation_time = json[c.DB_CREATION_TIME]  if c.DB_CREATION_TIME in json else time_now_str()
    self.rol = json[c.DB_ROL_KEY] if c.DB_ROL_KEY in json else c.NORMAL_USER

  def to_dict(self):
    return {c.DB_USERNAME_KEY: self.username,
            c.DB_PASSWORD_KEY: self.password,
            c.DB_EMAIL_KEY: self.email, 
            c.DB_ICON_KEY: self.icon,
            c.DB_CREATION_TIME: self.creation_time,
            c.DB_ROL_KEY: self.rol}