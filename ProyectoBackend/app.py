from logging import debug
import os
from flask import Flask, app
from flask_pymongo import PyMongo
import database
from dotenv import load_dotenv

from user.userModule import userModule

from pprint import pprint
import time

USERNAME = ""
PASSWORD = ""
BASE_URL = ""
MONGO_DB_URL = ""


def config_DB_URL():
    load_dotenv() #Cargar variables de entorno para acceder a los datos de la BD
    global USERNAME
    global PASSWORD
    global BASE_URL
    global MONGO_DB_URL

    USERNAME = os.getenv('DB_USERNAME')
    PASSWORD = os.getenv('DB_PASSWORD')
    BASE_URL = os.getenv('DB_URL')

    MONGO_DB_URL = "mongodb+srv://{0}:{1}@{2}/pad_tiermaker?retryWrites=true&w=majority".format(USERNAME, PASSWORD, BASE_URL)



#Inicialización del Servidor
config_DB_URL()

app = Flask(__name__)
app.config['MONGO_URI']=MONGO_DB_URL


database.mongo.init_app(app)

app.register_blueprint(userModule)


if __name__ == "__main__":
    app.run(debug=True)