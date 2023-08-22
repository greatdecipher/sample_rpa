from transform.manage_grade_transformation import MGT_CLASS_DICT

from loader.loader_automation import LoaderAutomation
from loader.loader_processor import LoaderProcessor

import web.manage_grade_automation as automation

from common.result_validator import validate_cbuild_csg
from filecleaner.filecleaner import fileCleaner
from emailer.emailer import eMailer

import logging
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)
import argparse
 
def manage_grades(file_id: str, psbuild_type: str):
    use_case = "Manage Grades"
    checkpoint = ""
    status = "Success"
    remark = ""

    def set_checkpoint(message):
        logging.info(f">>>>{message}")
        checkpoint = message

    logging.info(f"{'='*50}")
    set_checkpoint("Starting Manage Grades Function")
    logging.info(f"{'='*50}")
    try:
        set_checkpoint("Initializing and Validating Input File")
        transformation = MGT_CLASS_DICT[psbuild_type](file_id)

        if(transformation):
        # if True:
            set_checkpoint("Downloading Loader File")
            automation.get_loader_file(psbuild_type=psbuild_type, file_id=file_id)
            
            set_checkpoint("Initializing Loader Automation")
            loader_automation = LoaderAutomation(psbuild_type = psbuild_type, file_id = file_id)

            set_checkpoint("Launching Loader File")
            loader_automation.launch_loader()

            set_checkpoint("Initializing Loader Processor")
            loader_processor = LoaderProcessor(psbuild_type = psbuild_type, file_id = file_id)

            set_checkpoint("Validating Loader File")
            if loader_processor.validate_loader_file():
            # if True:
                set_checkpoint("Getting Grade Codes for Loading")
                grade_codes_list = transformation.get_grade_code_list()
                
                set_checkpoint("Downloading CSG Report for Loading")
                automation.get_csg_report(psbuild_type=psbuild_type, grade_codes=grade_codes_list, file_id=f"L_{file_id}")
                
                set_checkpoint("Downloading Sal Plan Report")
                automation.get_sal_plan_accum(psbuild_type=psbuild_type, file_id=file_id)

                set_checkpoint("Transforming PS Build to Cloud Build")
                transformation.start_transformation()
                
                set_checkpoint("Copying Cloud Build Data to Loader")
                loader_processor.copy_cb_to_loader()

                set_checkpoint("Uploading Loader Data")
                loader_automation.save_and_upload_file()
                
                set_checkpoint("Checking Status of Loader")
                loader_processor.status_checker(loader_automation)
                
                set_checkpoint("Closing Loader File")
                loader_automation.close_window("Save")
                
                set_checkpoint("Getting Grade Codes for Validation")
                grade_codes_list = transformation.get_grade_code_list()
                
                set_checkpoint("Downloading CSG Report for Validation")
                automation.get_csg_report(psbuild_type=psbuild_type, grade_codes=grade_codes_list, file_id=f"V_{file_id}")

                set_checkpoint("Validating CSG Report with Cloud Build Data")
                if not validate_cbuild_csg(file_id, psbuild_type):
                    remark = "Mismatched Data Found on Cloud Build File and CSG Report"
            else:
                status = "Failed"
                remark = "Invalid Loader File"
        else:
            status = "Failed"
            remark = transformation.error_remark   

        set_checkpoint("Initializing File Cleaner")
        fc = fileCleaner()

        set_checkpoint("Running File Cleaner")
        fc.filecleaner(use_case, file_id)
    except Exception as e:
        logging.exception(e, exc_info=True)
        status="Failed"
        remark = f"{remark}. Run Interrupted while {checkpoint} because of {str(type(e).__name__)}"
        raise(e)
    finally:
        logging.info(f"Status {status} with Remark: {remark}")  
        em = eMailer()
        em.emailer(use_case, file_id, status, remark) 
        
        print("-"*100)
    
if __name__=="__main__":
    parser = argparse.ArgumentParser(description='Runs Manage Grades Functionalities')
    parser.add_argument('--file_id', help='File ID', default="AMARI00_3020_MGWATB_20230727233646") #AMARI00_3005_MGWATB_20230726221923.xlsx
    parser.add_argument('--psbuild_type', help='PS Build type', default="WithATB")
    args = parser.parse_args()
    file_id = args.file_id
    psbuild_type = args.psbuild_type

    print("Manage Grades Process:")
    print(f"File ID:{file_id}")
    print(f"Build Type:{psbuild_type}")
    
    manage_grades(file_id, psbuild_type)
    