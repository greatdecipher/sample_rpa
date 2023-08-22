import os
from socket import timeout
import time
from commons.BaseScraper import BaseScraper
from commons.albertsons import AlbertsonsLogin
from commons.config import sal_plan_config
from commons.utils import get_timestamp, get_time_now
from commons.constants import Constants
from commons.ConfigFolder import get_process_folder_path


class SalaryPlan(BaseScraper):

    def __init__(self, page, context, settings_dict: dict = None):
        super(SalaryPlan, self).__init__(page=page, settings_dict=settings_dict)
        self.context = context
        self.page = page
        self.config = sal_plan_config
        self.settings_dict = settings_dict
        self.settings_dict['download_path'] = Constants.DOWNLOAD_PATH
        self.abs = AlbertsonsLogin(page, settings_dict=self.settings_dict)
        self.url_config = self.config['url']
        self.username_config = self.config['username']
        self.password_config = self.config['password']
        self.submit_button_config = self.config['submit_button']
        self.navbar_button_config = self.config['navbar_button']
        self.navbar_iframe_config = self.config['navbar_iframe']
        self.classic_home_tab_config = self.config['classic_home_tab']
        self.main_menu_dropdown_config = self.config['main_menu_dropdown']
        self.reporting_tools_config = self.config['reporting_tools']
        self.query_config = self.config['query']
        self.query_manager_config = self.config['query_manager']
        self.iframe_query_config = self.config['iframe_query']
        self.toolbar_div_config = self.config['toolbar_div']
        self.advance_search_btn_config = self.config['advance_search_btn']
        self.basic_search_btn_config = self.config['basic_search_btn']
        self.condition_option_config = self.config['condition_option']
        self.condition_value_config = self.config['condition_value']
        self.name_value_box_config = self.config['name_value_box']
        self.search_query_config = self.config['search_query']
        self.result_table_config = self.config['result_table']
        self.edit_button_config = self.config['edit_button']
        self.criteria_tab_config = self.config['criteria_tab']
        self.expression_columns_config = self.config['expression_columns']
        self.run_button_config = self.config['run_button']
        self.loader_config = self.config['loader']
        self.download_button_config = self.config['download_button']
        self.headless = self.settings_dict['headless']

    def run(self):
        self.logger.info(f"{self.psbuild_type}: Download report initiated...")
        self.abs.login_with_mfa_authentication(self.url_config)

        self.logger.info(f"{self.psbuild_type}: Logging in to Peoplesoft...")
        self.login()
        self.naviagte_to_file()
        return self.result_builder(self.settings_dict)

    def login(self):

        username = self.settings_dict['username']
        password = self.settings_dict['sal_plan_password']
        self.fill_element(self.username_config, username)
        self.fill_element(self.password_config, password)
        self.click_element(self.submit_button_config)
        time.sleep(5)

    def naviagte_to_file(self):
        self.click_element(self.navbar_button_config)
        self.get_iframe_content(self.navbar_iframe_config)
        self.click_iframe_element(self.classic_home_tab_config)
        self.click_element(self.main_menu_dropdown_config)

        self.click_element(self.reporting_tools_config)
        self.click_element(self.query_config)
        self.click_element(self.query_manager_config)
        time.sleep(5)
        self.get_iframe_content(self.iframe_query_config)
        self.click_iframe_element(self.advance_search_btn_config)
        time.sleep(1)
        self.select_option_within_iframe(self.condition_option_config, "contains")
        self.fill_iframe_element(self.name_value_box_config, Constants.SALARY_PLAN_ACCUM)
        self.click_iframe_element(self.search_query_config)
        self.click_iframe_element(self.edit_button_config)
        self.click_iframe_element(self.criteria_tab_config)
        self.click_iframe_element(self.expression_columns_config)
        self.click_iframe_element(self.run_button_config)

        start_time = get_time_now()
        end_time = get_time_now()
        duration = end_time - start_time

        self.logger.info(f"{self.psbuild_type}: Query is in loading state... Elapsed Time: {round(duration.total_seconds())}")
        while self.is_in_loading_state():
            end_time = get_time_now()
            duration = end_time - start_time
            if (round(duration.total_seconds()) % 30) == 0:
                self.logger.info(f"{self.psbuild_type}: Query is in loading state... Elapsed Time: {round(duration.total_seconds())} seconds")

        duration = end_time - start_time
        self.logger.info(f"{self.psbuild_type}: Query results have been loaded... Elapsed Time: {round(duration.total_seconds())} seconds")
        time.sleep(10)

        self.page.set_default_timeout(timeout=240000)

        if self.headless:
            self.logger.info(f"{self.psbuild_type}: Exporting in headless mode")
            with self.context.expect_page(timeout=0) as new_page_info:
                self.logger.info(f"{self.psbuild_type}: Expecting new tab for exporting...")
                self.click_iframe_element(self.download_button_config)
                time.sleep(1)
                new_page = new_page_info.value
                all_pages = self.context.pages
                self.page = all_pages[1]
                with self.page.expect_download(timeout=0) as download_info:
                    download = download_info.value
                    self.logger.info(f"{self.psbuild_type}: Exporting Data into excel. Filename: {download.suggested_filename}")

        else:
            self.logger.info(f"{self.psbuild_type}: Exporting in open browser mode")
            with self.page.expect_download(timeout=0) as download_info:
                with self.context.expect_page(timeout=0) as new_page_info:
                    self.logger.info(f"{self.psbuild_type}: Expecting new tab for exporting...")
                    self.click_iframe_element(self.download_button_config)
                    time.sleep(1)
            download = download_info.value

        file_path = get_process_folder_path(self.settings_dict['psbuild_type'])
        file_name = f"PSSalAcc_{self.settings_dict['file_name']}.xlsx"
        self.output_file = os.path.join(file_path, file_name)
        download.save_as(self.output_file)
        self.results['file_path'] = self.output_file
        time.sleep(3)

    def is_in_loading_state(self):
        while self.is_iframe_selector_visible(self.loader_config['selector']):
            return True
        return False

