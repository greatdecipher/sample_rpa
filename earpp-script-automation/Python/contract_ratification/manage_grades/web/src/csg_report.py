import os
import time
from commons.BaseScraper import BaseScraper
from commons.albertsons import AlbertsonsLogin
from commons.config import csg_config
from commons.utils import get_timestamp
from commons.constants import Constants
from commons.ConfigFolder import get_process_folder_path, get_environment


class CSGReport(BaseScraper):
    def __init__(self, page, context=None, settings_dict: dict = None):
        super(CSGReport, self).__init__(page=page, settings_dict=settings_dict)
        self.page = page
        self.config = csg_config
        self.settings_dict = settings_dict
        self.abs = AlbertsonsLogin(page, settings_dict=self.settings_dict)
        self.env = get_environment()
        self.url_config = self.config['url'][self.env]
        self.redirect_config = ""

        if self.settings_dict['psbuild_type'] in ["WithATB", "WithoutATB"]:
            self.redirect_config = self.config['csg_with_atb_url'][self.env]
        if self.settings_dict['psbuild_type'] == "ExistingATB":
            self.redirect_config = self.config['csg_existing_url'][self.env]

        self.iframe = self.config['iframe']
        self.group_code_config = self.config['group_code_dropdown']
        self.search_option_config = self.config['search_option']
        self.grade_codes = self.settings_dict['grade_codes']
        self.search_item_box_config = self.config['search_item_box']
        self.search_button_config = self.config['search_button']
        self.search_item_result_config = self.config['search_result_item']
        self.item_020_config = self.config['search_result_item_020']
        self.move_item_button_config = self.config['move_item_button']
        self.okay_button_config = self.config['okay_button']
        self.review_and_apply_button_config = self.config['review_and_apply_button']
        self.export_icon_button_config = self.config['export_icon_button']
        self.excel_export_button_config = self.config['excel_export_button']
        self.home_button_config = self.config['home_button']
        self.data_not_found_config = self.config['data_not_found']
        self.no_data_found = []
        self.data_found = []

    def run(self):
        self.logger.info(f"{self.psbuild_type}: Initiated download - CSG Report.")
        self.abs.login_with_mfa_authentication(self.url_config)
        time.sleep(8)
        self.redirect_to()
        return self.result_builder(self.settings_dict)

    def redirect_to(self):
        self.logger.info(f"{self.psbuild_type}: Redirecting to url: {self.redirect_config['value']}")
        self.navigate(self.redirect_config)
        time.sleep(10)
        self.search_and_move_item()

    def search_and_move_item(self):
        self.get_iframe_content(self.iframe)

        self.click_iframe_element(self.group_code_config)
        self.click_iframe_element(self.search_option_config)

        for gc in self.grade_codes:
            self.logger.info(f"{self.psbuild_type}: Search Grade Code: {gc}")
            self.fill_iframe_element(self.search_item_box_config, gc)
            self.click_iframe_element(self.search_button_config)
            time.sleep(2)

            if self.is_iframe_selector_visible(self.data_not_found_config['selector']):
                self.no_data_found.append(gc)
            else:
                self.data_found.append(gc)
                self.click_iframe_element(self.search_item_result_config)
                self.click_iframe_element(self.move_item_button_config)
                self.logger.info(f"{self.psbuild_type}: Move Grade Code: {gc}")

        self.click_iframe_element(self.okay_button_config)

        if len(self.data_found) > 0:
            self.download_csg_report()
        else:
            self.results["results_not_found"] = self.no_data_found
            self.results["results_found"] = self.data_found
            time.sleep(3)


    def download_csg_report(self):
        self.logger.info(f"{self.psbuild_type}: Exporting CSG Report")
        with self.page.expect_download() as download_info:
            self.click_iframe_element(self.export_icon_button_config)
            self.click_iframe_element(self.excel_export_button_config)

        download = download_info.value
        file_path = get_process_folder_path(self.settings_dict['psbuild_type'])
        file_name = f"CSGRep{self.settings_dict['file_name']}.xlsx"
        self.output_file = os.path.join(file_path, file_name)
        download.save_as(self.output_file)
        self.results['file_path'] = self.output_file

        self.logger.info(f"Finished exporting CSG Report file: {self.output_file}")
        time.sleep(3)


