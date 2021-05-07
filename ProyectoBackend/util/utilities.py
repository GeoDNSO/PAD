import time
import datetime

#named_tuple = time.localtime() # get struct_time
#time_string = time.strftime("%m/%d/%Y, %H:%M:%S", named_tuple)

def time_now_str():
    time_string = datetime.datetime.utcnow().strftime("%m/%d/%Y, %H:%M:%S")
    return time_string