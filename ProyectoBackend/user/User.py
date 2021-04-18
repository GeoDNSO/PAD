import constants as c

class User:
  def __init__(self, username, password="", email=""):
    self.username = username
    self.password = password
    self.email = email

    def to_json(self):
        return {c.DB_USERNAME_KEY: self.username,
                c.DB_PASSWORD_KEY: self.password,
                c.DB_EMAIL_KEY: self.email}