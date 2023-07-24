link_management = {
    'common_albertsons': 'https://albehcm-dev.opc.albertsons.com/psp/ALBEHDEV/?cmd=login&languageCd=ENG',
    'existing_atb' : 'https://eofd.fa.us6.oraclecloud.com/analytics/saw.dll?bipublisherEntry&Action=open&itemType=.xdo&bipPath=%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo&path=%2Fshared%2FCustom%2FHuman%20Capital%20Management%2FWorkforce%20Management%2FPublisher%20Objects%2FReport%2FCSG_Manage%20Existing%20ATB%20Report.xdo',
    'with_atb' : '',
    'without_atb': '',
    'data_exchange': 'https://eofd.fa.us6.oraclecloud.com/hcmUI/faces/FndOverview?fnd=%3B%3B%3B%3Bfalse%3B256%3B%3B%3B&fndGlobalItemNodeId=itemNode_workforce_management_data_exchange&_adf.ctrl-state=30d8gy3in_1&_adf.no-new-window-redirect=true&_afrLoop=1349124877220069&_afrWindowMode=2&_afrWindowId=null&_afrFS=16&_afrMT=screen&_afrMFW=1280&_afrMFH=609&_afrMFDW=1280&_afrMFDH=720&_afrMFC=8&_afrMFCI=0&_afrMFM=0&_afrMFR=144&_afrMFG=0&_afrMFS=0&_afrMFO=0',
    'dev_spreadsheets': 'https://eofd-dev10.fa.us6.oraclecloud.com/hcmUI/faces/FndOverview?fnd=%3B%3B%3B%3Bfalse%3B256%3B%3B%3B&fndGlobalItemNodeId=itemNode_workforce_management_data_exchange&_afrLoop=1619264466686511&_afrWindowMode=0&_afrWindowId=lvvxun6ci&_adf.ctrl-state=1a21m5fx5h_1&_afrFS=16&_afrMT=screen&_afrMFW=1280&_afrMFH=609&_afrMFDW=1280&_afrMFDH=720&_afrMFC=8&_afrMFCI=0&_afrMFM=0&_afrMFR=144&_afrMFG=0&_afrMFS=0&_afrMFO=0',
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
    'pass_word' : 'Qkgu_8HCna',
    'gen_key' : '62hnq7fcnbhs5bfr',
}


excel_title = {
    'file_one':'Loader_CSG_Manage Existing ATB(2).xlsx',
}


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
    title_max = "Maximize"
    title_min = "Minimize"
    title_restore = "Restore"
    auto_id_password = "passwordInput"
    auto_id_review = "TabReview"
    auto_id_sheet = "SheetProtect"
    auto_id_tabsheet = "SheetTab"
    auto_id_ok = "m_btnOK"
    control_item = "TabItem"
    control_button = "Button"
    sso_button = "Company Single Sign-On"
    found_index = 0

class CellValues:
    loader_eff_start = '* Effective Start Date'
    loader_atb_rate = 'ATB Rate'
    cloud_eff_start = 'Effective Start Date'
    cloud_atb_rate = 'ATB Rate'
    leftmost_col = 'Changed'
    rightmost_col = 'Key'

class Labels:
    loader_existing_atb = "Loader_Existing"
    loader_with_atb = "Loader_with"
    loader_without_atb = "Loader_Without"
    cloud_existing = "Cloud_Existing"
    cloud_with = "Cloud_With"
    cloud_without = "Cloud_Without"

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
    sample_excel = r"./excel_files\Loader_CSG_Manage Existing ATB(4).xlsx"
    sample_excel_two = r"./excel_files\Loader_CSG_Manage Existing ATB(2).xlsx"
    test_existing_atb_loader = r"./test_files\Loader_CSG_Manage Existing ATB.xlsx"
    test_with_atb_loader = r"./test_files\Loader_CSG_Manage Grades with ATB.xlsx"
    test_without_atb_loader = r"./test_files\Loader_CSG_Manage Grades without ATB.xlsx"
    test_existing_cloud = r"./test_files\Cloud Build Existing ATB Loader.xlsx"
    test_with_cloud = r"./test_files\Cloud Build With ATB Loader.xlsx"
    test_without_cloud = r"./test_files\Cloud Build Without ATB Loader.xlsx"
    match_existing_loader = r"./test_files\LOADER Manage Existing ATB - Matched File.xlsx"
    cloud_build_existing = r"./test_files\Cloud Build existing ATB.xlsx"
    cloud_build_with = r"./test_files\Cloud Build with ATB.xlsx"
    cloud_build_without = r"./test_files\Cloud Build wo ATB.xlsx"

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