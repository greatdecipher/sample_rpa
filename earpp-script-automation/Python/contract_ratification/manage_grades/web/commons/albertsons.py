import time

import pyotp
from commons.config import abs_config
from BaseScraper import BaseScraper
from playwright.sync_api import Page

import logging

class AlbertsonsLogin(BaseScraper):
    def __init__(self, page, settings_dict: dict):
        super(AlbertsonsLogin, self).__init__(page=page, settings_dict=settings_dict)
        self.page = page
        self.config = abs_config
        self.settings_dict = settings_dict
        self.email_input_config = self.config['ldap_email']
        self.next_button_config = self.config['next_button']
        self.login_error_config = self.config['error_login']
        self.username_input_config = self.config['ldap_username']
        self.password_input_config = self.config['ldap_password']
        self.login_button_config = self.config['ldap_login_button']
        self.otp_input_config = self.config['otp_input_box']
        self.otp_error_config = self.config['otp_error_text']
        self.otp_verify_button_config = self.config['verify_otp_btn']
        self.sso_button_config = self.config['sso_btn']

    def login_with_mfa_authentication(self, url_config: dict):
        self.navigate_to(url_config)
        self.login()

    def navigate_to(self, url_config):
        self.logger.info(f"{self.psbuild_type}: Navigate to {url_config['value']}")
        self.navigate(url_config)


    def single_sign_on(self):

        if self.is_selector_visible(self.sso_button_config['selector']):
            self.click_element(self.sso_button_config)
            return True

        return False


    def login(self):
        email = self.settings_dict['email']
        username = self.settings_dict['username']
        password = self.settings_dict['password']
        time.sleep(5)

        if self.is_selector_visible(self.sso_button_config['selector']):
            self.logger.info("Single Sign-on Login")
            self.click_element(self.sso_button_config)
        time.sleep(5)

        if self.is_selector_visible(self.email_input_config['selector']):
            self.logger.info(f"{self.psbuild_type}: Login with email: {email}")
            self.fill_element(self.email_input_config, email)
            self.click_element(self.next_button_config)
            time.sleep(3)

        if self.is_selector_visible(self.login_error_config['selector']):
            self.error_results = True
            return self.result_builder(self.login_error_config)

        self.logger.info(f"{self.psbuild_type}: Login with username: {username}")
        self.fill_element(self.username_input_config, username)
        self.fill_element(self.password_input_config, password)
        self.click_element(self.login_button_config)

        if self.is_selector_visible(self.login_error_config['selector']):
            self.error_results = True
            return self.result_builder(self.login_error_config)

        self.authenticate_with_otp()
        time.sleep(3)

        while self.is_selector_visible(self.otp_error_config['selector']):
            self.authenticate_with_otp()

        self.logger.info(f"{self.psbuild_type}: Authenticated user with username: {username}")
    def authenticate_with_otp(self):
        otp = self.generate_otp()
        self.logger.info(f"{self.psbuild_type}: Using generated OTP: {otp}")
        self.fill_element(self.otp_input_config, otp)
        self.click_element(self.otp_verify_button_config)

    def generate_otp(self):
        secret = self.settings_dict['otp_secret']
        totp = pyotp.TOTP(secret)
        return totp.now()
