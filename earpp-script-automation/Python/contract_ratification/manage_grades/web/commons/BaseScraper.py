from os import path
import pyotp
import sys
import datetime
import json
import logging

from absLogger import AbsLogger
from ConfigFolder import screenshot_path, get_log_path
from playwright.sync_api import TimeoutError as PlaywrightTimeoutError, Page

sys.tracebacklimit = -1

logging.basicConfig(
    level=logging.INFO,
    format='\x1b[38;5;39m' + "%(asctime)s [%(levelname)s] %(message)s" + '\x1b[0m',
    handlers=[
        logging.FileHandler(get_log_path()),
        logging.StreamHandler(),
    ]
)


class BaseScraper:

    logging = logging

    def __init__(self, page: Page, settings_dict=None):
        self.page = page
        self.start_time = datetime.datetime.utcnow()
        self.end_time = None
        self.results = dict()
        self.error_results = False
        self.logger = logging
        self.iframe = None
        self.psbuild_type = f"Manage Grade {settings_dict['file_type']} {settings_dict['psbuild_type']}"
        self.output_file = None
        self.file_id = settings_dict['file_name']


    def navigate(self, url_config: dict):
        try:
            url = url_config['value']
            self.page.goto(url)

        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(url_config)

    def fill_element(self, config: dict, value):

        try:
            selector = config["selector"]
            self.page.fill(selector, value)

        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def click_element(self, config: dict):
        try:
            self.page.click(config['selector'])
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def fill_iframe_element(self, config: dict, value):
        try:
            selector = config["selector"]
            self.iframe.fill(selector, value)

        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def click_iframe_element(self, config: dict):
        try:
            self.iframe.click(config['selector'], timeout=120000)
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def select_option_within_iframe(self, config: dict, label):
        try:
            menu_selector = config['selector']
            label = label
            self.iframe.select_option(menu_selector, label=label)
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def get_iframe_content(self, config: dict):
        try:
            iframe = self.query_element(config)
            self.iframe = iframe.content_frame()
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def press_element(self, config: dict, key: str):
        try:
            self.page.press(config['selector'], key)
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def get_element(self, config: dict) -> list:
        try:
            selector = config['selector']
            element = self.page.locator(selector)
            return element
        except PlaywrightTimeoutError as e:
            self.error_results = True
            self.logger.error(f"{self.psbuild_type}: {e}")
            return self.result_builder(config)

    def get_child_element_text(self, element, config: dict) -> str:
        try:
            selector = config['selector']
            text = element.locator(selector).all_text_contents()

            return text

        except:
            self.error_results = True
            return self.result_builder(config)

    def wait_for_element(self, config: dict):

        try:
            el = self.page.wait_for_selector(config["selector"])
            if el:
                return True
        except:
            error = config["error_results"]
            error["selector"] = config["selector"]
            return error

    def get_otp(self, secret: str):
        totp = pyotp.TOTP(secret)
        return totp.now()

    def is_selector_visible(self, selector: str):
        if self.page.locator(selector).is_visible():
            return True

        return False

    def is_iframe_selector_visible(self, selector: str):
        if self.iframe.locator(selector).is_visible():
            return True

        return False

    def query_element(self, config: dict):
        try:
            selector = config['selector']
            element = self.page.query_selector(selector)
            return element

        except:
            self.logger(f"Element not found with selector: {config['selector']}")
            self.error_results = True
            return self.result_builder(config)

    def get_element_attribute(self, config: dict, value: str):
        try:
            selector = config['selector']
            element = self.page.query_selector(selector)
            attribute_value = element.get_attribute(value)
            return attribute_value

        except:
            self.error_results = True
            return self.result_builder(config)


    def result_builder(self, config):

        if self.error_results:
            error = config["error_results"]
            error["selector"] = config["selector"]
            error["start_time"] = self.start_time
            error["end_time"] = datetime.datetime.utcnow()
            duration = error['end_time'] - error['start_time']
            error["duration"] = duration.total_seconds()
            self.screenshot(self.file_id)
            print(json.dumps(error, indent=4, sort_keys=True, default=str))
            return error

        self.results["status"] = "Completed"
        self.results["start_time"] = self.start_time
        self.results["end_time"] = datetime.datetime.utcnow()
        duration = self.results['end_time'] - self.results['start_time']
        self.results["duration"] = duration.total_seconds()
        print(json.dumps(self.results, indent=4, sort_keys=True, default=str))
        return self.results

    def screenshot(self, file_name):
        path = f"{screenshot_path()}\{file_name}"
        print(path)
        self.page.screenshot(path=f"{path}.png")
