from pywinauto.application import Application
from pywinauto import Desktop
from config import ExcelFilePath
import win32gui
import pywinauto.findwindows as p
import time
import logging
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

class TestExcelWindow:
    def __init__(self):
        self.window_title = "GenericHdlSpreadsheet Loader MG e ATB  -  Protected View - Excel"
        self.path_xlsx = r".\newfiles/GenericHdlSpreadsheet Loader MG e ATB.xlsx"
        self.app = Application(backend="uia").start(r'{} "{}"'.format(
                            ExcelFilePath.excel_app, self.path_xlsx))
        self.excel_window = self.app.window(title=self.window_title)
        self.top_windows = []

    def launcher(self):
        app = self.app
        logging.info("Trying to connect...")
    
    def check_open_windows(self):
        windows = Desktop(backend="uia").windows()
        open_windows = [window for window in windows]
        logging.info(open_windows)

    def initializer_logs(self, seconds):
        for i in range(seconds):
            logging.info(f"Connecting to Excel App...{i}")
            time.sleep(1)

    def minimize_window(self):
        self.excel_window.minimize()
        logging.info("Minimized Excel......")

    def set_foreground(self):
        handle = win32gui.FindWindow(0, self.window_title)
        win32gui.SetForegroundWindow(handle)
        logging.info("Foregrounded...........")

    def all_windows(self):
        logging.info(p.enum_windows())
        logging.info("Returns list of window handles")

    
if __name__=="__main__":
    existingATB = TestExcelWindow()
    existingATB.launcher()
    existingATB.initializer_logs(5)
    existingATB.launcher()
    existingATB.initializer_logs(5)
    existingATB.check_open_windows()
    existingATB.initializer_logs(5)
    existingATB.minimize_window()
    existingATB.initializer_logs(5)
    existingATB.set_foreground()

    
