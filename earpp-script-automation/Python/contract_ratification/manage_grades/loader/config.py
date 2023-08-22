
link_management = {
    'common_albertsons': 'https://albehcm-dev.opc.albertsons.com/psp/ALBEHDEV/?cmd=login&languageCd=ENG',
    'existing_atb' : 'https://eofd.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo',
    'with_atb' : '',
    'without_atb': '',
    'data_exchange': 'https://eofd.fa.us6.oraclecloud.com/hcmUI/faces/FndOverview?fnd=%3B%3B%3B%3Bfalse%3B256%3B%3B%3B&fndGlobalItemNodeId=itemNode_workforce_management_data_exchange&_adf.ctrl-state=30d8gy3in_1&_adf.no-new-window-redirect=true&_afrLoop=1349124877220069&_afrWindowMode=2&_afrWindowId=null&_afrFS=16&_afrMT=screen&_afrMFW=1280&_afrMFH=609&_afrMFDW=1280&_afrMFDH=720&_afrMFC=8&_afrMFCI=0&_afrMFM=0&_afrMFR=144&_afrMFG=0&_afrMFS=0&_afrMFO=0',
    'dev_spreadsheets': 'https://eofd-dev8.fa.us6.oraclecloud.com/hcmUI/faces/FndOverview?fnd=%3B%3B%3B%3Bfalse%3B256%3B%3B%3B&fndGlobalItemNodeId=itemNode_workforce_management_data_exchange&_afrLoop=1619264466686511&_afrWindowMode=0&_afrWindowId=lvvxun6ci&_adf.ctrl-state=1a21m5fx5h_1&_afrFS=16&_afrMT=screen&_afrMFW=1280&_afrMFH=609&_afrMFDW=1280&_afrMFDH=720&_afrMFC=8&_afrMFCI=0&_afrMFM=0&_afrMFR=144&_afrMFG=0&_afrMFS=0&_afrMFO=0',
}

selectors_management = {
    'sso_button': '#ssoBtn',
    'dev10_filluser': '#i0116',
    'next_button': '#idSIButton9',
    'input_pass': '#passwordInput',
    'submit_login' : 'span#submitButton',
    'fill_mfa': 'input#idTxtBx_SAOTCC_OTC',
    'continue_otp': 'input#idSubmit_SAOTCC_Continue',
    'grade_code_search' : 'span#xdo\:parameters > table > tbody > tr:nth-child(1) > td:nth-child(3) > label > span',
    'spreadsheet_template': 'a#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:_FOTsr1\:0\:ll01Upl\:UPsp1\:ll01Pce\:ll01Itr\:1\:ll02Pce\:ll01Lv\:1\:ll01Pse\:ll01Cl',
    'csg_withoutATB': 'a:has-text("CSG_Manage Grades without ATB")',
    'csg_withATB':'a:has-text("CSG_Manage Grades with ATB")',
    'csg_existingATB': 'a:has-text("CSG_Manage Existing ATB")',
    'click_body': '#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:SPph\:\:_afrStr',
    'search_templatebar': '#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:_LSSF\:\:content',
    'preview_button': 'button#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:2\:AP1\:gb1',
    'search_button' : '#_FOpt1\:_FOr1\:0\:_FONSr2\:0\:MAnt2\:1\:AP1\:ls1\:_LSSB > img',
}
filter_text = {
    'search_without_atb': 'CSG_Manage Grades without ATB',
    'search_with_atb': 'CSG_Manage Grades with ATB',
    'search_existing': 'CSG_Manage Existing ATB',
}

creds = {
    'user_name' : 'erey118@safeway.com',
    'user': 'erey118',
    'password': 'Qkgu_8HCna',
    'otp_secret': '62hnq7fcnbhs5bfr',
}



# class Encryption:
#     def __init__(self, required_creds):
#         self.required_creds = required_creds
    
#     def main(self):
#     # Create an instance of the EncryptedConfigManager class
#         file_path = r"../credential\encrypted_config.dat"  # Assuming you have already stored the encrypted data in this file
#         config_manager = EncryptedConfigHandler(file_path)

#         # Retrieve the decrypted config data (assuming it's a JSON object)
#         decrypted_config = config_manager.get_decrypted_config()

#         # Now you can access the decrypted config data as a Python dictionary
#         decrypted_config[self.required_creds]
            
# if __name__ == "__main__":
#     password = Encryption('password')
#     password.main()
#     otp_secret = Encryption('otp_secret')
#     otp_secret.main()


class PywinConfig:
    title_enable = "Enable Editing"
    title_review = "Review"
    title_unprotect = "Unprotect Sheet..."
    title_spreadsheet = "Spreadsheet Loader"
    title_dataset = "Create Data Set"
    title_ok = "OK"
    title_close = "Close"
    title_dont_save = "Don't Save"
    title_save = "Save"
    title_upload = "Upload"
    title_max = "Maximize"
    title_min = "Minimize"
    title_restore = "Restore"
    title_more = "More"
    title_refresh = "Refresh"
    auto_id_password = "passwordInput"
    auto_id_review = "TabReview"
    auto_id_sheet = "SheetProtect"
    auto_id_tabsheet = "SheetTab"
    auto_id_ok = "m_btnOK"
    control_item = "TabItem"
    control_button = "Button"
    control_menu = "MenuItem"
    sso_button = "Company Single Sign-On"
    found_index = 0

class CellValues:
    loader_eff_start = '* Effective Start Date'
    loader_atb_rate = 'ATB Rate'
    cloud_eff_start = '* Effective Start Date m/d/yyyy'
    cloud_atb_rate = 'ATB Rate'
    leftmost_col = 'Changed'
    rightmost_col = 'Key'


class Colors:
    """ ANSI color codes """
    RED = "\u001b[31m"
    GREEN = "\u001b[32m"
    YELLOW = "\u001b[33m"
    BLUE = "\u001b[34m"
    MAGENTA = "\u001b[35m"
    CYAN = "\u001b[36m"
    RESET = "\u001b[0m"

class ExcelFilePath:
    excel_app = r"C:\Program Files\Microsoft Office\root\Office16\EXCEL.EXE"

    test_existing_atb_loader = r"./loader\newfiles\GenericHdlSpreadsheet Loader MG e ATB.xlsx"
    test_with_atb_loader = r"./loader\newfiles\GenericHdlSpreadsheet Loader MG w ATB.xlsx"
    test_without_atb_loader = r"./loader\newfiles\GenericHdlSpreadsheet Loader MG wo ATB.xlsx"

    test_without_atb_loader = r"./test_files\Loader_CSG_Manage Grades without ATB.xlsx"

    new_cloud_eatb_data = r"./loader\newfiles\Cloud Build - Manage Grades with Existing ATB.xlsx"
    new_cloud_watb_data = r"./loader\newfiles\Cloud Build - Manage Grades with ATB.xlsx"
    new_cloud_woatb_data = r"./loader\newfiles\Cloud Build - Manage Grades without ATB.xlsx"

    
base_loaders = {
    "WithATB": r"./loader\newfiles\Cloud_loader_wATB.xlsx",
    "WithoutATB": r"./loader\newfiles\Cloud_loader_woATB.xlsx", 
    "ExistingATB": r"./loader\newfiles\Cloud_loader_eATB.xlsx",
}

pgrade_base_loaders = {
    "NewRecord": "pgrade_base_NewRecord.xlsx",
    "ExistingRecord": "pgrade_base_ExistingRecord.xlsx",
}

pgrade_column_names_coordinates = {
    "ExistingRecord": "F26:J26",
    "NewRecord": "F26:R26",
}

column_names_coordinates = {
    "WithATB": "F26:W26",
    "WithoutATB": "F26:P26",
    "ExistingATB": "F26:N26",
}

pgrade_column_name_coor = {
    'NewRecord':'F26:R26',
    'ExistingRecord':'F26:J26',
}

status_comment = {
    'start':"Starting Application",
    'connect':"Connecting Window",
    'enable': "Clicked enable editing",
    'click_sso': "Clicked Company Signon button",
    'dialog_box':"Dialog Box, Clicked Yes",
    'click_erey': "Clicked EREY acct",
    'review': "Clicked Review Button",
    'unprotect':"Sheet Unprotected",
    'pass':"Typed Password",
    'submit':"Click Submit button",
    'mfa':"Input MFA code",
    'verify':"Clicked Verify",
    'spreadsheet': "Clicked Spreadsheet Loader",
    'dataset': "Data Set Creation",
    'ok_invoke': "Clicked 'OK'",
    'dataset_success': "Data Set Created Successfully",
    'closing':"Attempt To Close Application",
    'Not Saved': "Changes were not saved",
    'Saved Changes':"Saved Changes",
    'min': "Minimized Window",
    'max':"Maximized Window",
    'resized':"Resized window",
    'more': "Clicked 'More'",
    'upload': "Clicked Upload Button",
    'refresh': "Clicked Refresh",
}
pgrade_column_mapping_dict = {
    'ExistingRecord':{
        '* Effective Start Date':'Effective Start Date',
        'Effective End Date':'Effective End Date',
        'Grade Ladder Name':'Grade Ladder Name',
        'Sequence':'Sequence',
        'Grade Code':'Grade Code',
    },

    'NewRecord':{
        '* Effective Start Date':'Effective Start Date',
        'Effective End Date': 'Effective End Date',
        'Grade Ladder Group':'Grade Ladder Group',
        'Grade Ladder Name':'Grade Ladder Name',
        'Status':'Status',
        'Grade Step Transaction Date':'Grade Step Transaction Date',
        'Grade Step Transaction Date Formula Name':'Grade Step Transaction Date Formula Name',
        'Grade Step Confirmation Type': 'Grade Step Confirmation Type',
        'Salary Update Transaction Date':'Salary Update Transaction Date',
        'Rate Synchronization Transaction Date':'Rate Synchronization Transaction Date',
        'Rate Synchronization Confirmation Type':'Rate Synchronization Confirmation Type',
        'Sequence': 'Sequence',
        'Grade Code': 'Grade Code',
    },
}
column_mapping_dict = {
    'WithATB':  {
        '* Effective Start Date': 'Effective Start Date',
        'Effective End Date': 'Effective End Date',
        '* Set Code': 'Set Code',
        '* Grade Code': 'Grade Code',
        'Grade Name': 'Grade Name',
        'Active Status': 'Active Status',
        'Step Number': 'Step Number',
        '* Step Name': 'Step Name',
        'Ceiling Step': 'Ceiling Step',
        'Progression Hours Accumulator': 'Progression Hours Accumulator',
        'InformationCategory': 'Information Category',
        'Sequence Number': 'Sequence Number',
        '* EFF_CATEGORY_CODE': 'EFF_CATEGORY_CODE',
        'ATB Effective Date': 'ATB Effective Date',
        'ATB Rate': 'ATB Rate',
        'ATB Length of Service (Min Years)': 'ATB Length of Service (Min Years)',
        'ATB Length of Service (Max Years)': 'ATB Length of Service (Max Years)',
        'Replace First Effective Start Date': 'Replace First Effective Start Date',
    } ,

    'ExistingATB': {
        '* Effective Start Date': 'Effective Start Date',
        'Effective End Date': 'Effective End Date',
        '* Set Code' : 'Set Code',
        '* Grade Code': 'Grade Code',
        'Sequence Number': 'Sequence Number',
        '* EFF_CATEGORY_CODE': 'EFF_CATEGORY_CODE',
        'Grade Legislative ID [..]': 'Grade Legislative ID',
        'ATB Effective Date': 'ATB Effective Date',
        'ATB Rate': 'ATB Rate',

    },

    'WithoutATB': {
        '* Effective Start Date': 'Effective Start Date',
        'Effective End Date': 'Effective End Date',
        '* Set Code': 'Set Code',
        '* Grade Code': 'Grade Code',
        'Grade Name': 'Grade Name',
        'Active Status': 'Active Status',
        'Step Number': 'Step Number',
        '* Step Name': 'Step Name',
        'Ceiling Step': 'Ceiling Step',
        'Progression Hours Accumulator': 'Progression Hours Accumulator',
        'Replace First Effective Start Date': 'Replace First Effective Start Date',
    }

}

matcher = {
    'editing': "Enable Editing",
    'sso': "Company Single Sign-On",
    'erey': "Sign in with erey118@safeway.com work or school account.",
    'edit': "Edit",
    'login':"idTxtBx_SAOTCC_OTC",
    'continue':"idSubmit_SAOTCC_Continue",
    'pass':"passwordInput",
}

class ManageFilePath:
    processfolderExistingATB = r"\\earpprpaqast01.file.core.windows.net\fileshare-rpa-coe\Argus\ContractRatification\ManageGrades\ExistingATB\Processing"
    processfolderWithATB = r"\\earpprpaqast01.file.core.windows.net\fileshare-rpa-coe\Argus\ContractRatification\ManageGrades\WithATB\Processing"
    processfolderWithoutATB = r"\\earpprpaqast01.file.core.windows.net\fileshare-rpa-coe\Argus\ContractRatification\ManageGrades\WithoutATB\Processing"
    loader_log_path = r"\\earpprpaqast01.file.core.windows.net\fileshare-rpa-coe\Argus\ContractRatification\TestData\ManageGrades\Testing_logs\app.log"

