from flask import Blueprint
from flask import request
from flask_pymongo import PyMongo
from pymongo import cursor, errors
from database import mongo

from .Template import Template
from . import templateUtils
from util.utilities import time_now_str
from flask import current_app as app

import constants
from bson.objectid import ObjectId

from util.utilities import getCustomArgs, getPageAndLimit
import base64
import os, errno
import uuid
import shutil

#Consultas

#returns templates order by creation date desc
def getTemplates(args):

    page, limit = getPageAndLimit(args)
    custom_args = getCustomArgs(args).to_dict()
    
    print(custom_args)

    cursor = mongo.db.templates.find(custom_args).sort( [['_id', -1]] ).skip((page-1)*limit).limit(limit)
    return templateUtils.listFromCursor(cursor)

def getPopularTemplates(args):

    page, limit = getPageAndLimit(args)
    custom_args = getCustomArgs(args).to_dict()

    group_option = {"$group": 
                            {
                            "_id": f"${constants.DB_TEMPLATE_ID}", 
                            "count": {"$sum": 1}
                            }
                    }

    sort = {"$sort": {"count":-1}}

    cursor = mongo.db.tiers_done.aggregate([group_option, sort])
    listCount = templateUtils.tierListInfoFromCursor(cursor)

    custom_args["_id"] = {"$in": listCount} #add if filter --> id_filter = {"_id": {"$in": listCount}}

    cursor = mongo.db.templates.find(custom_args).skip((page-1)*limit).limit(limit)

    return templateUtils.listFromCursor(cursor)


#Manejo de ficheros y directorios

def deleteDirectory(templateID):
    folderPath = os.path.dirname(os.path.join(app.root_path , app.config['UPLOAD_FOLDER'], templateID, ""))
    shutil.rmtree(folderPath)

#Modificar diccionario con las URLs correctas
def convertB64Images(templateDict):

    #Create Directory for template from its _id
    folderName = templateDict[constants.DB_ID_KEY]
    folderPath = os.path.dirname(os.path.join(app.root_path , app.config['UPLOAD_FOLDER'], folderName, ""))
    createUniqueDirectory(folderPath)
    
    #Image filename
    fileName = constants.DB_COVER_KEY  + ".png/"
    pathCoverName = os.path.dirname(os.path.join(folderPath, fileName))

    #Create cover image from template
    createFileFromB64(pathCoverName, templateDict[constants.DB_COVER_KEY])
    templateDict[constants.DB_COVER_KEY] = "/" + folderName + '/' + fileName

    #Create images from container
    templateContainerImages = templateDict[constants.DB_CONTAINER_KEY]
    i = 0
    for i in range(len(templateContainerImages)):
        #fileName = str(i) + str(uuid.uuid4()) + ".png/"
        fileName = str(i) + ".png/"
        filePath = os.path.dirname(os.path.join(folderPath, fileName))
        createFileFromB64(filePath, templateContainerImages[i])
        templateContainerImages[i] = "/" + folderName + "/" + fileName

    return templateDict

def createFileFromB64(path, data):
    with open(path, "wb+") as image_file:
        image_file.write(base64.b64decode(data))

#Modifies path Folde if exits
def createUniqueDirectory(pathFolder):
    ok = createDirectory(pathFolder)

    while not ok:
        pathFolder = str(uuid.uuid4())
        createDirectory(pathFolder)

def createDirectory(pathFolder):
    
    try:
        os.mkdir(pathFolder)
    except OSError as e:
        print ("Creation of the directory {0} failed".format(pathFolder))
        if e.errno != errno.EEXIST:
            raise
        else:
            return False
    else:
        print ("Successfully created the directory {0}".format(pathFolder))
    
    return True