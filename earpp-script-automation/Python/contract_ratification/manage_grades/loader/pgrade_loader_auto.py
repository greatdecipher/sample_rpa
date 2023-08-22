from pywinauto.application import Application
from pywinauto import Desktop
from loader.config import status_comment, matcher, creds, ExcelFilePath
from loader_automation import LoaderAutomation
from getMfacode import mfa_code

import time
import os
import configparser
import win32gui
from pathlib import Path
environment = os.environ.get('ENV', 'DEV')

import sys
import subprocess
import logging
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)


class PGLLoaderAutomation(LoaderAutomation):
    def __init__(self, psbuild_type, file_id):
        super().__init__(psbuild_type, file_id)
        self.pgl_initial_window_title = f"Loader_{self.file_id.rpartition('/')[-1]}  -  Protected View - Excel"


    def launch_loader(self):
        self.window_title = self.pgl_initial_window_title
        window_title = self.window_title
        logging.info(f"{'='*30} Launching {window_title} {'='*30}")
        self.app = Application(backend="uia").start(r'{} "{}"'.format(
                    ExcelFilePath.excel_app, self.path_xlsx)) 
        app = self.app       
        logging.info("Trying to connect...")
        self.countdown(10)
        app = self.attempt_function(self.connect_app, window_title)
        excel_window = app.window(title=window_title)
        self.excel_window = excel_window
        
        try:
            enable_edit_btn = excel_window.child_window(best_match="Enable Editing")
            logging.info("Trying to Click Enable Editing")
            self.attempt_function(self.click_button, excel_window, enable_edit_btn, attempts=1)

        except Exception as e:
            logging.exception(e)
            logging.info("Enable Editing not found")
        
        self.window_title = f"Loader_{self.file_id.rpartition('/')[-1]} - Excel"
        window_title = self.window_title
        app = self.attempt_function(self.connect_app, window_title)
        excel_window = app.window(title=window_title)
        self.excel_window = excel_window

        connect_yes_btn = excel_window.Connect.Yes
        self.attempt_function(self.click_dialog_button, connect_yes_btn)

        self.login_loader()

        self.attempt_function(self.unprotect_sheet, excel_window)
        self.attempt_function(self.invoke_worksheet_action, excel_window, "Create Data Set")
        self.countdown(5)
        try:
            download_ok_btn = excel_window.child_window(title="Download").Yes
            self.attempt_function(self.click_dialog_button, download_ok_btn)
        except Exception as e:
            logging.exception(e)
            logging.info("No Download Dialog Found")

        self.attempt_function(self.invoke_worksheet_action, excel_window, "Refresh")

        try:
            invoke_ok_btn = excel_window.child_window(title="Invoke Action").OK
            self.attempt_function(self.click_dialog_button, invoke_ok_btn)
        except Exception as e:
            logging.exception(e)
            logging.info("Under Investigation")

        self.excel_window = excel_window

        logging.info(f"{'='*30} Triggered Save and Upload {'='*30}")