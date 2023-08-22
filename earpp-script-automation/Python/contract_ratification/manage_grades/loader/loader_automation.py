from pywinauto.application import Application
from pywinauto import Desktop
from loader_creds import creds
from loader.config import status_comment, matcher, ExcelFilePath, ManageFilePath
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
logging.basicConfig(filename = ManageFilePath.loader_log_path, filemode = 'w', format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

class LoaderAutomation:
    def __init__(self, psbuild_type, file_id, excel_view="Excel"):
        self.file_id = file_id
        config = configparser.ConfigParser()
        config_file_path = os.path.join(str(Path(__file__).parents[1]),'manage_grades.ini')
        config.read(config_file_path)
        
        process_folder = str(config.get(f"{environment}.ManageGrades", 'processfolder' + psbuild_type))
        subprocess.Popen('reg add HKCU\Software\Microsoft\Windows\CurrentVersion\Explorer\Advanced /v HideFileExt /t REG_DWORD /d 1 /f', shell=True).wait()

        self.excel_view = excel_view
        self.window_title = f"Loader_{self.file_id.rpartition('/')[-1]} - {excel_view}"
        self.path_xlsx = f'{process_folder}/Loader_{file_id}.xlsx'

        self.app = None
        self.excel_window = None

    def countdown(self, secs):
        for i in range(secs, 0, -1):
            sys.stdout.write(f"\rSleeping for {str(i)} sec(s)                        \r")
            sys.stdout.flush()
            time.sleep(1)

    def connect_app(self, window_title):
        logging.info(f"Connecting to {window_title} window")
        return Application(backend="uia").connect(title=window_title)

    def login_loader(self):
        logging.info(f"{'.'*30} Logging In {'.'*30}")
        excel_window = self.excel_window
        has_previous_login = self.check_previous_login()
        try:
            login_sso_btn = excel_window.Login.child_window(best_match="Company Single Sign-On")
            self.attempt_function(self.click_dialog_button, login_sso_btn, attempts=5)
        except Exception as e:
            if has_previous_login:
                logging.info("Already Logged In")
            else: 
                logging.info("Something Unexpected. Under Investigation.")

        self.countdown(5)
        login_window = excel_window.Login
        if login_window.exists() and os.getlogin() != 'EREY118' and not has_previous_login:
            try:
                login_user_btn = self.excel_window.Login.child_window(best_match=matcher['erey'])
                self.attempt_function(self.click_dialog_button,  login_user_btn, attempts=5)
                logging.info(status_comment['click_erey'])
                self.countdown(5)

                login_pass_edit = excel_window.Login.child_window(auto_id=matcher['pass'],control_type=matcher['edit'])
                self.attempt_function(self.set_text, login_pass_edit, creds('password'), attempts=5)
                logging.info(status_comment['pass'])
                self.countdown(5)

                login_submit_btn = self.excel_window.Login.child_window(title="Sign in", auto_id="submitButton",
                                control_type="Button")
                self.attempt_function(self.click_dialog_button, login_submit_btn, attempts=5)
                logging.info(status_comment['submit'])
                self.countdown(5)

                self.verify_otp()
            except Exception as e:
                logging.info("No Further Login Process needed.")

    def verify_otp(self):
        mfa_edit = self.excel_window.Login.child_window(auto_id=matcher['login'],
                        control_type=matcher['edit'])
        self.attempt_function(self.set_text, mfa_edit, mfa_code(creds('otp_secret')))
        logging.info(status_comment['mfa'])

        continue_btn = self.excel_window.Login.child_window(title="Verify", auto_id=matcher['continue'], 
                        control_type="Button")
        self.attempt_function(self.click_dialog_button, continue_btn, attempts=5)
        logging.info(status_comment['verify'])

    def launch_loader(self):
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

    def click_dialog_button(self, param_btn):
        if param_btn.exists():
            self.attempt_function(self.click_button, self.excel_window, param_btn)
            self.countdown(5)
            try:
                self.click_dialog_button(param_btn)
            except Exception as e:
                return
        else:
            logging.info(f"{param_btn.element_info.name} not found.")
            raise Exception(f"{param_btn.element_info.name} not found.")

        logging.info("Dialog Button Clicked.")

    def set_text(self, edit_box, edit_text):
        logging.info(f"{'.'*30} Setting Text {'.'*30}")
        window_title = self.window_title
        handle = win32gui.FindWindow(0, window_title)
        win32gui.SetForegroundWindow(handle)
        edit_box.set_text(edit_text)
        self.countdown(2)

    def check_previous_login(self):
        logging.info("Checking Previous Login from Excel Windows")
        is_existing = False
        windows = Desktop(backend="uia").windows()
        open_excel_windows = [window.window_text() for window in windows if window.element_info.class_name == 'XLMAIN' and self.window_title != window.window_text()]
        logging.info(open_excel_windows)
        for open_excel_window in open_excel_windows:
            open_app = self.connect_app(open_excel_window)
            open_window = open_app.window(title=open_excel_window)
            logging.info(f"Checking {open_excel_window} if already signed-in")
            is_existing = is_existing | open_window.child_window(title="Efren Reyes (Contractor)", class_name="NetUIAnchor").exists()
            if is_existing:
                logging.info(f"{open_excel_window} already Logged in")
                return is_existing

        return is_existing

    def save_and_upload_file(self):
        logging.info(f"{'='*30} Saving and Upload Data {'='*30}")
        excel_window = self.excel_window

        self.attempt_function(self.select_worksheet_item, excel_window, "Save")
        try:
            self.countdown(5)
            upload_ok_btn = excel_window.child_window(title="Upload Options").OK
            self.attempt_function(self.click_dialog_button, upload_ok_btn)
        except Exception as e:
            logging.exception(e)
            logging.info("Under Investigation")

        self.attempt_function(self.select_worksheet_item, excel_window, "Upload")
        try:
            self.countdown(5)
            invoke_ok_btn = excel_window.child_window(title="Invoke Action").OK 
            self.attempt_function(self.click_dialog_button, invoke_ok_btn)
        except Exception as e:
            logging.exception(e)
            logging.info("Under Investigation")
        
        self.refresh_upload()
        logging.info(f"{'='*30} Launched Succesfully {'='*30}")

    def refresh_upload(self):
        excel_window = self.excel_window
        self.attempt_function(self.invoke_worksheet_action, excel_window, "Refresh")

    def attempt_function(self, param_func, *args, attempts=20):
        logging.info(f"{'.'*30} Attempting to run {param_func.__name__} Function {'.'*30}")
        for attempt in range(attempts):
            try:
                logging.info(f"Attempt no. {str(attempt + 1)}")
                logging.info(f"Function Name: {param_func.__name__}")
                logging.info(f"No of Arguments Passed: {len(args)}")

                # while self.check_open(self.window_title):
                #     logging.info("Another Excel File is open with Dialog Box")

                return param_func(*args)
            except Exception as e:
                logging.info("Something Happened. Retrying...")
                logging.exception(e)
                self.countdown(5)
                continue
            break
        logging.info(f"Used all {attempts} Attempts")
        raise Exception("Element not Found")

    def check_open(self, window_title):
        logging.info("Checking Open Excel Windows if Busy")
        is_busy = False
        windows = Desktop(backend="uia").windows()
        
        open_windows = [window for window in windows]
        logging.info(open_windows)

        open_excel_windows = [window.window_text() for window in windows if window.element_info.class_name == 'XLMAIN' and window_title != window.window_text()]
        logging.info(open_excel_windows)
        curr_priority_no = 0
        
        if self.excel_window:
            curr_priority_no = self.get_priority_value(self.excel_window)

        self_priority_no = curr_priority_no

        for open_excel_window in open_excel_windows:
            open_app = self.connect_app(open_excel_window)
            open_window = open_app.window(title=open_excel_window)
            logging.info(f"Checking {open_excel_window}")
            open_priority_no = self.get_priority_value(open_window)
            
            if open_priority_no > curr_priority_no:
                is_busy = True
                curr_priority_no = open_priority_no

        if curr_priority_no == self_priority_no:
            logging.info("Free to Proceed")

        return is_busy

    def get_priority_value(self, param_window):
        window_name = param_window.element_info.name
        is_existing = False
        
        priority_list = [
            "No Priority Item",
            "Connect",
            "Login",
            "Protect",
            "Worksheet",
            "Invoke Action",
            "More",
            "Upload Options",
            "Microsoft Excel"
        ]
        
        while param_window.child_window(title="Microsoft Excel", control_type="Window", class_name="NetUINetUIDialog").exists():
            logging.info(f"{window_name} still choosing Closing Options")
            return priority_list.index("Microsoft Excel")

        while param_window.child_window(title="Invoke Action", control_type="Window").exists():
            logging.info(f"{window_name} still Invoking Action")
            return priority_list.index("Invoke Action")

        while param_window.child_window(title="Upload Options", control_type="Window").exists():
            logging.info(f"{window_name} still choosing Upload Options")
            return priority_list.index("Upload Options")

        spreadsheet_more_item = param_window.child_window(title="More", class_name="NetUIAnchor")
        more_menu = spreadsheet_more_item.child_window(title="More", control_type="Menu")
        while more_menu.exists():
            logging.info(f"{window_name} still doing More Spreadsheet Loader Functions")
            return priority_list.index("More") 

        worksheet_grp = param_window.child_window(title="Worksheet", class_name="NetUIChunk")
        while worksheet_grp.exists():
            logging.info(f"{window_name} still doing Spreadsheet Loader Functions")
            return priority_list.index("Worksheet") 

        protect_grp = param_window.child_window(title="Protect", class_name="NetUIChunk")
        while protect_grp.exists():
            logging.info(f"{window_name} still doing Review Functions")
            return priority_list.index("Protect")
        
        while param_window.child_window(title="Login", control_type="Window").exists():
            logging.info(f"{window_name} still Logging in")
            return priority_list.index("Login")

        while param_window.child_window(title="Connect", control_type="Window").exists():
            logging.info(f"{window_name} still Connecting")
            return priority_list.index("Connect")

        logging.info(f"{priority_list.index('No Priority Item')} No Priority Item")
        return priority_list.index("No Priority Item")
        
    def click_button(self, excel_window, param_btn):
        logging.info(f"{'.'*30} Clicking {param_btn.element_info.name} {'.'*30}")
        window_title = self.window_title
        handle = win32gui.FindWindow(0, window_title)
        win32gui.SetForegroundWindow(handle)
        param_btn.invoke()

    def switch_tab(self, excel_window, tab_title):
        logging.info(f"{'.'*30} Switching to {tab_title} Tab {'.'*30}")
        next_tab = excel_window.child_window(title=tab_title, class_name="NetUIRibbonTab")
        next_tab.select()

    def close_window(self, closing_option):
        logging.info(f"{'='*30} Closing Window with {closing_option} Option {'='*30}")
        excel_window = self.excel_window
        excel_window.close()
        window_title = self.window_title
        handle = win32gui.FindWindow(0, window_title)
        win32gui.SetForegroundWindow(handle)
        closing_window = excel_window.child_window(title="Microsoft Excel", control_type="Window", class_name="NetUINetUIDialog")
        closing_option_btn = closing_window.child_window(title=closing_option)
        self.attempt_function(self.click_button, excel_window, closing_option_btn)

        logging.info(f"{'='*30} Closed Succesfully {'='*30}")

    def unprotect_sheet(self, excel_window):
        logging.info(f"{'.'*30} Unprotecting Sheet {'.'*30}")
        self.switch_tab(excel_window, "Review")

        protect_menu = excel_window.child_window(title="Protect", control_type="MenuItem")
        unprotect_parent_window = excel_window
        if protect_menu.exists():
            protect_menu.expand()
            unprotect_parent_window = protect_menu

        protect_grp = unprotect_parent_window.child_window(title="Protect", class_name="NetUIChunk")
        unprotect_btn = protect_grp.child_window(title="Unprotect Sheet...")
        unprotect_btn.invoke()
        self.switch_tab(excel_window, "Home")

    def invoke_worksheet_action(self, excel_window, action_title):
        logging.info(f"{'.'*30} Invoking {action_title} in Spreadsheet Loader {'.'*30}")
        self.switch_tab(excel_window, "Spreadsheet Loader")
        worksheet_grp = excel_window.child_window(title="Worksheet", class_name="NetUIChunk")
        action_btn = worksheet_grp.child_window(title=action_title, control_type="Button")
        action_btn.invoke()
        logging.info(f"Clicked {action_title}")
        self.switch_tab(excel_window, "Home")

    def select_worksheet_item(self, excel_window, item_name):
        logging.info(f"{'.'*30} Selecting {item_name} in Spreadsheet Loader {'.'*30}")
        self.switch_tab(excel_window, "Spreadsheet Loader")

        spreadsheet_more_item = excel_window.child_window(title="More", class_name="NetUIAnchor")
        more_sub_item = spreadsheet_more_item.child_window(title=item_name, control_type="MenuItem")
        spreadsheet_more_item.expand()
        more_sub_item.select()

        self.switch_tab(excel_window, "Home")