from playwright.sync_api import sync_playwright
from getMfacode import mfa_code
from loader.config import link_management, selectors_management, creds, filter_text
import time
import os

class PlayPageTools:
    def __init__(self):
        pass
    def downloader(page,download_button,name_convention):
        with page.expect_download() as download_info:
            page.locator(download_button).click()
        download = download_info.value
        file_name = f"Loader_{name_convention}(%s).xlsx"
        destination_folder = "./excel_files"
        i = 0
        while os.path.exists(destination_folder +'/'+ file_name % i):
            i += 1
        path_xlsx = os.path.join(destination_folder, file_name % i)
        download.save_as(path_xlsx)
        return path_xlsx
        

class PlayLoaderLink:
    def prod_or_dev8(page,goto_link):
        if goto_link == link_management['dev_spreadsheets'] :
            page.goto(link_management['dev_spreadsheets'])
            page.click(selectors_management['sso_button'])
            page.fill(selectors_management['dev10_filluser'], creds['user_name'])
            page.click(selectors_management['next_button'])
            page.fill(selectors_management['input_pass'], creds['pass_word'])
        
        if goto_link == link_management['data_exchange']:
            page.goto(link_management['data_exchange'])
            page.fill('input#userNameInput', creds['user_name'])
            page.fill('input#passwordInput', creds['pass_word'])


    def different_loaders(page,filter,click_text):
        page.locator(selectors_management['search_templatebar']).fill('')
        page.fill(selectors_management['search_templatebar'], filter)
        page.click(selectors_management['search_button'])
        time.sleep(5)
        page.click(selectors_management['click_body'])
        page.locator(click_text).click()
        name_convention = page.locator(click_text).inner_text()
        return name_convention

class PlaywrightRun:
    def __init__(self):
        self.path_xlsx = None
    def start_app(self):
        with sync_playwright() as p:
            browser = p.chromium.launch(headless=False, slow_mo=50)
            page = browser.new_page()
            PlayLoaderLink.prod_or_dev8(page,link_management['dev_spreadsheets'])
            #PlayLoaderLink.prod_or_dev8(link_management['data_exchange'])
            page.click(selectors_management['submit_login'])
            page.fill(selectors_management['fill_mfa'], mfa_code(creds['gen_key']))
            page.click(selectors_management['continue_otp'])
            page.click(selectors_management['spreadsheet_template'])
            #name_convention = PlayLoaderLink.different_loaders(page,filter_text['search_without_atb'],
            #               selectors_management['csg_withoutATB'])
            name_convention = PlayLoaderLink.different_loaders(page,filter_text['search_with_atb'],
                            selectors_management['csg_withATB'])
            #name_convention = PlayLoaderLink.different_loaders(page,filter_text['search_existing'],
            #               selectors_management['csg_existingATB'])
            self.path_xlsx = PlayPageTools.downloader(page,selectors_management['preview_button'],name_convention)
            
            time.sleep(10)

if __name__=="__main__":
    pass




