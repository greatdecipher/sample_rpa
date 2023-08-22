from loader.config import status_comment, matcher, creds, PywinConfig, ExcelFilePath, excel_title

import os
import configparser
from pathlib import Path
environment = os.environ.get('ENV', 'DEV')

from pywinauto.application import Application

config = configparser.ConfigParser()
config_file_path = os.path.join(str(Path(__file__).parent),'manage_grades.ini')
config.read(config_file_path)

psbuild_type = "WithATB"
file_id = "AMARI00_3003_MGWATB_20230726215246"

process_folder = str(config.get(f"DEV.ManageGrades", 'processfolder' + psbuild_type))

path_xlsx = f'{process_folder}/Loader_{file_id}.xlsx'

app = Application(backend="uia").start(r'{} "{}"'.format(
                    ExcelFilePath.excel_app, path_xlsx))

window_title = f"{path_xlsx.rpartition('/')[-1]} - Excel"
# excel_window = app.window(title_re=window_title)
# excel_window.kill(soft=False)
# import time
# time.sleep(20)
# print(app.top_window())

app.send_keys("{VKMENU}{F4}")

# app.top_window().child_window(title_re="Close").click()
# keyboard.press("Enter")
# excel_window.window(title_re="Upload Options").window(title_re='OK').click()