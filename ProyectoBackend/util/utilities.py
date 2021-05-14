import time
import datetime
import constants

#named_tuple = time.localtime() # get struct_time
#time_string = time.strftime("%m/%d/%Y, %H:%M:%S", named_tuple)

def time_now_str():
    time_string = datetime.datetime.utcnow().strftime("%d/%m/%Y,%H:%M:%S,UTC+00")
    return time_string

def getCustomArgs(args):
    custom_args = args.copy()
    custom_args.pop(constants.PAGINATION_PAGE)
    custom_args.pop(constants.PAGINATION_LIMIT)
    return custom_args

def getPageAndLimit(args):
    page = 1
    limit = 1
    try:
        page = int(args[constants.PAGINATION_PAGE])
        limit = int(args[constants.PAGINATION_LIMIT])
    except:
        page = 1
        limit = 1
    return page, limit