from os import path

from flask import jsonify
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo

from flask import current_app as app

import constants
from bson.objectid import ObjectId

import base64
import os
import uuid


#Modificar diccionario con las URLs correctas
def convertB64Images(templateDict):

    #Create Directory for template from its _id
    folder = templateDict[constants.DB_ID_KEY]
    pathFolder = os.path.dirname(os.path.join(app.root_path , app.config['UPLOAD_FOLDER'], folder, ""))
    createDirectory(pathFolder)

    #Image filename
    fileName = constants.DB_COVER_KEY  +".png/"
    pathCoverName = os.path.dirname(os.path.join(pathFolder, fileName))
    print("El path es: ", pathCoverName)

    #Create cover image from template
    templateCover = templateDict[constants.DB_COVER_KEY]
    createFileFromB64(pathCoverName, templateCover)
    templateDict[constants.DB_COVER_KEY] = "/" + folder + '/' + fileName

    templateContainerImages = templateDict[constants.DB_CONTAINER_KEY]
    print(templateContainerImages)
    i = 0
    for i in range(len(templateContainerImages)):
        fileName = str(i) + str(uuid.uuid4()) + ".png/"
        filePath = os.path.dirname(os.path.join(pathFolder, fileName))
        createFileFromB64(filePath, templateContainerImages[i])
        templateContainerImages[i] = "/" + folder + "/" + fileName

    print("Diccionario en Helper")
    print(templateDict)
    return templateDict

def createFileFromB64(path, data):
    with open(path, "wb+") as image_file:
        image_file.write(base64.b64decode(data))

def createDirectory(pathFolder):
    
    try:
        os.mkdir(pathFolder)
    except OSError:
        print ("Creation of the directory {0} failed".format(pathFolder))
    else:
        print ("Successfully created the directory {0}".format(pathFolder))
    