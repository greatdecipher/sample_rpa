from cmath import log
import os
import configparser
from pathlib import Path
from utils import get_timestamp


def get_process_folder_path(psbuild_type):

    config = read_config_file()
    environment = get_environment()
    process_path = config.get(f"{environment}.ManageGrades", f"processfolder{psbuild_type}")
    return process_path


def get_environment():
    config = read_config_file()
    environment = config.get("MainSection", "environment")

    return environment


def read_config_file():
    config = configparser.ConfigParser()
    config_file_path = os.path.join(str(Path(__file__).parents[2]), 'manage_grades.ini')
    config.read(config_file_path)

    return config

def screenshot_path():
    config = read_config_file()
    environment = get_environment()
    screenshot_path = config.get(f"{environment}.ManageGrades", "screenshots")

    return screenshot_path

def get_log_path():
    config = read_config_file()
    environment = get_environment()
    log_dir = config.get(f"{environment}.LoggerSection", "logPathWeb")

    if not os.path.exists(log_dir):
        os.makedirs(log_dir)

    log_path = os.path.join(log_dir, f"web_{get_timestamp()}.log")

    print(log_path)
    return log_path

