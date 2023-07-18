
from pywinauto.application import Application
from config import status_comment, matcher, creds, PywinConfig, ExcelFilePath
from getMfacode import mfa_code
import time

#automate excel with pywinauto
class PywinStartExcel:
    def excel_launch(path_xlsx):
        app = Application(backend="uia").start(r'{} "{}"'.format(
                    ExcelFilePath.excel_app, path_xlsx))
        print(status_comment['start'])
        time.sleep(10)

        print(status_comment['connect'])
        time.sleep(30)
        app.Dialog.child_window(best_match="Enable Editing").click()

        time.sleep(10)
        app.Dialog.Connect.Yes.click()

        time.sleep(40)


        app.Dialog.Login.child_window(best_match="Company Single Sign-On").click()
        print(status_comment['click_sso'])
        time.sleep(10)
        
        app.Dialog.Login.child_window(best_match=matcher['erey']).click()
        print(status_comment['click_erey'])
        time.sleep(10)

        app.Dialog.Login.child_window(auto_id=matcher['pass'],
                        control_type=matcher['edit']).set_text(creds['pass_word'])
        print(status_comment['pass'])
        time.sleep(10)

        app.Dialog.Login.child_window(title="Sign in", auto_id="submitButton",
                        control_type="Button").click()
        print(status_comment['submit'])
        time.sleep(10)

        app.Dialog.Login.child_window(auto_id=matcher['login'],
                        control_type=matcher['edit']).set_text(mfa_code(creds['gen_key']))
        print(status_comment['mfa'])
        time.sleep(10)

        app.Dialog.Login.child_window(title="Verify", auto_id=matcher['continue'], 
                        control_type="Button").click()
        print(status_comment['verify'])

        PywindowTools.print_timer()

        #click on 'Review' button.
        PywinParameter.parameter_main_filter(app,PywinConfig.title_review,
                        PywinConfig.auto_id_review, PywinConfig.control_item,
                        status_comment['review'])
 
        #click on 'Unprotect Sheet' button.
        PywinParameter.parameter_main_filter(app,PywinConfig.title_unprotect,
                        PywinConfig.auto_id_sheet,PywinConfig.control_button,
                        status_comment['unprotect'])

        #click on 'Spreadsheet Loader'
        PywinParameter.parameter_with_index(app,PywinConfig.title_spreadsheet,
                        PywinConfig.control_item, PywinConfig.found_index,
                        status_comment['spreadsheet'])
        
        #click on 'Create Data Set'
        PywinParameter.parameter_with_index(app,PywinConfig.title_dataset,
                        PywinConfig.control_button, PywinConfig.found_index,
                        status_comment['dataset'])

        #click on 'OK' on Invoke Action Dialog box, twice pop up.
        for i in range(2):
            PywinParameter.parameter_dialog_box(app,PywinConfig.title_ok,
                            PywinConfig.auto_id_ok, PywinConfig.control_button,
                            status_comment['ok_invoke']) 
        
        #PywindowTools.min_max_restore(app,False)
        #PywindowTools.close_window(app,PywinConfig.title_dont_save)
        time.sleep(10)
        



class PywinParameter:  
    def parameter_with_index(app, title, control_type, indexing, comment):
        time.sleep(5)
        check_button = app.Dialog.child_window(title=title, control_type=control_type,
                        found_index=indexing).wrapper_object()
        PywindowTools.clicker(check_button, comment)

    def parameter_main_filter(app, title, auto_id, control_type, comment):
        time.sleep(5)
        try:
            check_button = app.Dialog.child_window(title=title, auto_id=auto_id,
                            control_type=control_type).wrapper_object()
            PywindowTools.clicker(check_button, comment)
        except:
            pass

    def parameter_dialog_box(app, title, auto_id, control_type, comment):
        time.sleep(5)
        check_button = app.Dialog.InvokeAction.child_window(title=title, auto_id=auto_id,
                        control_type=control_type).wrapper_object()
        PywindowTools.clicker(check_button, comment)

    def if_child_exists(app, title, control_type, indexing):
        time.sleep(5)
        check_button = app.Dialog.child_window(title=title, control_type=control_type,
                        found_index=indexing).exists(timeout=20)
        return check_button



class PywindowTools:
    def print_timer():
        for i in range(20,0,-1):
            print(str(i))
            time.sleep(1)

    def control_identifiers(app):
        app.Dialog.print_control_identifiers()

    def clicker(review_button, comment):
        time.sleep(5)
        review_button.click_input()
        print(comment)
        time.sleep(5)

    def close_window(app,title):
        PywinParameter.parameter_with_index(app,PywinConfig.title_close,
                        PywinConfig.control_button, PywinConfig.found_index,
                        status_comment['closing'])
        
        if title == PywinConfig.title_dont_save:
           PywinParameter.parameter_with_index(app,PywinConfig.title_dont_save,
                        PywinConfig.control_button, PywinConfig.found_index,
                        status_comment['Not Saved'])
        
        elif title == PywinConfig.title_save:
            PywinParameter.parameter_with_index(app,PywinConfig.title_save,
                        PywinConfig.control_button, PywinConfig.found_index,
                        status_comment['Saved Changes'])
    

    def min_max_restore(app,headless):
        if headless == False:
            if PywinParameter.if_child_exists(app,PywinConfig.title_max,
                            PywinConfig.control_button,PywinConfig.found_index) == True :

                PywinParameter.parameter_with_index(app,PywinConfig.title_max,
                            PywinConfig.control_button, PywinConfig.found_index,
                            status_comment['max'])

            elif PywinParameter.if_child_exists(app,PywinConfig.title_restore,
                            PywinConfig.control_button,PywinConfig.found_index) == True:

                PywinParameter.parameter_with_index(app,PywinConfig.title_restore,
                            PywinConfig.control_button, PywinConfig.found_index,
                            status_comment['resized'])
        elif headless == True:
            PywinParameter.parameter_with_index(app,PywinConfig.title_min,
                            PywinConfig.control_button, PywinConfig.found_index,
                            status_comment['min'])


