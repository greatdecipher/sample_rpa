import time
from playwright.sync_api import sync_playwright, Page
from src.loader import Loader
from src.csg_report import CSGReport
from src.salary_plan import SalaryPlan
from commons.constants import Constants
from commons.BaseScraper import BaseScraper
from commons.config import ldap_config
from commons.ConfigFolder import get_process_folder_path, get_environment


# ExistingATB, WithATB, WithoutATB

logging = BaseScraper.logging
headless = True

if get_environment() == "DEV":
    headless = False


def start_browser_engine(App, settings_dict):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=headless, args=['--start-maximized'])
        context = browser.new_context(no_viewport=True)
        page = context.new_page()
        application = App(page, context=context, settings_dict=settings_dict)
        return application.run()


def get_loader_file(psbuild_type: str = None, file_id: str = None):
    '''
    Method to download loader file based on PS BUILD TYPE
    :params:
    :return: dict
    '''
    config_dict = ldap_config
    config_dict["psbuild_type"] = psbuild_type
    config_dict["file_name"] = file_id
    config_dict["file_type"] = Constants.LOADER_FILE

    print(f"Starting download {Constants.LOADER_FILE}: {psbuild_type}")

    app = start_browser_engine(Loader, config_dict)
    return app


def get_csg_report(psbuild_type: str = None, grade_codes: list = None, file_id: str = None):
    '''
    Method to download CSG REPORT file based on GRADE CODES
    :params: dict
    "sample_params: {
            "psbuild_type": "WitATB",
            "grade_codes": ['AC05-101', 'AC05-102', 'AC05-104', 'AC05-107', 'AC05-108',
                            'AC05-109', 'AC05-110', 'AC05-111', 'AC05-112', 'AC05-113']
        }
    :return: dict
    '''

    if psbuild_type == "WithoutATB" and file_id.startswith("L"):
        print({"message": f"{psbuild_type} CSG Report for these records are not found", "status": "Skipped"})
        return

    config_dict = ldap_config
    config_dict["psbuild_type"] = psbuild_type
    config_dict["grade_codes"] = grade_codes
    config_dict["file_name"] = file_id
    config_dict["file_type"] = Constants.CSG_FILE

    print(f"Starting download {Constants.CSG_FILE}: {psbuild_type}")
    app = start_browser_engine(CSGReport, config_dict)
    return app

def get_sal_plan_accum(psbuild_type: str = None, file_id: str = None):
    '''
    Method to download SALARY GRADE PLAN file from peoplesoft
    :params:
    :return: dict
    '''

    config_dict = ldap_config
    config_dict["psbuild_type"] = psbuild_type
    config_dict["file_name"] = file_id
    config_dict["file_type"] = Constants.SAL_PLAN_FILE
    config_dict["headless"] = headless
    print(f"Starting download {Constants.SAL_PLAN_FILE}: {psbuild_type}")

    app = start_browser_engine(SalaryPlan, config_dict)
    return app
