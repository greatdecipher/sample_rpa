
from loader.detect_transfer_bot import TransferExcelData
import time

from loader.loader_automation import LoaderAutomation
from loader.loader_processor import LoaderProcessor
import argparse

from common.result_validator import validate_cbuild_csg

parser = argparse.ArgumentParser(description='Runs Manage Grades Functionalities')
parser.add_argument('--file_id', help='File ID', default="amari00_112_MGEATB_20230726192601") #amari00_112_MGEATB_20230726192601 has cloud build
parser.add_argument('--psbuild_type', help='PS Build type', default="ExistingATB")
args = parser.parse_args()
file_id = args.file_id
psbuild_type = args.psbuild_type

loader_automation = LoaderAutomation(psbuild_type = psbuild_type, file_id = file_id)
loader_automation.launch_loader()

loader_processor = LoaderProcessor(psbuild_type = psbuild_type, file_id = file_id)
print(loader_processor.validate_loader_file())
loader_processor.copy_cb_to_loader()

loader_automation.save_and_upload_file()
# validate_cbuild_csg(psbuild_type = psbuild_type, file_id = file_id)
#loader_automation.close_window("Don't Save")

# loader_excel = TransferExcelData(psbuild_type = psbuild_type, file_id = file_id)
# # loader_excel.copy_cb_to_loader()
# loader_automation.save_and_upload_file()
# # loader_automation.close_window("Don't Save")

# psbuild_type = "WithATB"
# file_id = "[LDAPID]_[Count]_MGwATB_[timestamp]"

# loader_automation2 = LoaderAutomation(psbuild_type = psbuild_type, file_id = file_id)
# loader_automation2.launch_loader()

# loader_automation.close_window("Don't Save")
# loader_automation2.close_window("Don't Save")

# pywin_excel = PywinStartExcel(psbuild_type = psbuild_type, file_id = file_id)
# #pywin_excel.excel_launch()
# print(pywin_excel.path_xlsx)

# loader_excel = TransferExcelData(psbuild_type = psbuild_type, file_id = file_id)
# print(loader_excel.path_excel)
# loader_excel.validate_loader_file()
# loader_excel.loader_conditional()
# pywin_excel.save_and_upload_file()
# loader_excel.status_checker(pywin_excel)

# time.sleep(15)


