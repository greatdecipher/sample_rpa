import datetime


def get_timestamp():
    date = datetime.datetime.utcnow()
    date_str = date.strftime("%Y%m%d")

    return date_str

def get_time_now():
    return datetime.datetime.utcnow()

def get_filename(filename:str):

    file_name = filename.split(".")[0]

    return file_name
