import os
import time
from commons.BaseScraper import BaseScraper
from commons.albertsons import AlbertsonsLogin
from commons.config import loader_config
from commons.utils import get_timestamp
from commons.constants import Constants
from commons.ConfigFolder import get_process_folder_path, get_environment


class Loader(BaseScraper):

    def __init__(self, page, context=None, settings_dict: dict = None):
        super(Loader, self).__init__(page=page, settings_dict=settings_dict)
        self.page = page
        self.config = loader_config
        self.settings_dict = settings_dict
        self.abs = AlbertsonsLogin(page, settings_dict=self.settings_dict)
        self.env = get_environment()
        self.url_config = self.config['url'][self.env]
        self.group_node_config = self.config['group_node_tab']
        self.data_exchange_config = self.config['data_exchange_card']
        self.template_config = self.config['template_link']
        self.clear_button_config = self.config['clear_button']
        self.keyword_box_config = self.config['keyword_input_box']
        self.search_button_config = self.config['search_button']
        self.div_click_config = self.config['div_click']
        self.report_link_config = self.config['report_link']
        self.preview_button_config = self.config['preview_button']
        self.home_button_config = self.config['home_button']

        if self.settings_dict['psbuild_type'] == "WithATB":
            self.build_type = Constants.MANAGE_GRADE_WITH_ATB
        if self.settings_dict['psbuild_type'] == "WithoutATB":
            self.build_type = Constants.MANAGE_GRADE_WITHOUT_ATB
        if self.settings_dict['psbuild_type'] == "ExistingATB":
            self.build_type = Constants.MANAGE_GRADE_EXISTING_ATB


    def run(self):
        self.logger.info(f"{self.psbuild_type}: Download report initiated...")
        self.abs.login_with_mfa_authentication(self.url_config)
        self.navigate_manage_grade_loader()
        self.logger.info(f"Finished downloading {self.settings_dict['psbuild_type']} Report {self.output_file}")
        return self.result_builder(self.settings_dict)

    def navigate_manage_grade_loader(self):
        self.logger.info(f"{self.psbuild_type}: Navigating to report")
        self.click_element(self.home_button_config)
        self.click_element(self.group_node_config)
        self.click_element(self.data_exchange_config)
        self.click_element(self.template_config)
        self.click_element(self.clear_button_config)
        self.fill_element(self.keyword_box_config, self.build_type)
        self.click_element(self.search_button_config)
        time.sleep(3)
        self.click_element(self.div_click_config)
        self.click_element(self.report_link_config)
        time.sleep(5)

        self.download_loader_file()

    def download_loader_file(self):
        self.logger.info(f"{self.psbuild_type}: Exporting report")
        with self.page.expect_download() as download_info:
            self.click_element(self.preview_button_config)

        download = download_info.value
        file_path = get_process_folder_path(self.settings_dict['psbuild_type'])
        file_name = f"Loader_{self.settings_dict['file_name']}.xlsx"
        self.output_file = os.path.join(file_path, file_name)
        download.save_as(self.output_file)
        self.results['file_path'] = self.output_file
        time.sleep(3)