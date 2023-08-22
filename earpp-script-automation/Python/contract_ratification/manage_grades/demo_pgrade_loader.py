from loader.pgrade_loader_processor import LoaderProcessor
from loader.pgrade_loader_auto import PGLLoaderAutomation
from pywinauto import Desktop
import argparse
import logging


parser = argparse.ArgumentParser(description='Runs Manage Grades Functionalities')
parser.add_argument('--file_id', help='File ID', default="NewRecord") #AMARI00_3005_MGWATB_20230726221923.xlsx
parser.add_argument('--psbuild_type', help='PS Build type', default="NewRecord")
args = parser.parse_args()
file_id = args.file_id
psbuild_type = args.psbuild_type


# psbuild_type = "ExistingATB"
# file_id = "[LDAPID]_[Count]_MGeATB_[timestamp]"
loader_automation = PGLLoaderAutomation(psbuild_type = psbuild_type, file_id = file_id)
loader_automation.launch_loader()

loader_processor = LoaderProcessor(psbuild_type = psbuild_type, file_id = file_id)
print(loader_processor.validate_loader_file())
loader_processor.copy_cb_to_loader()

loader_automation.save_and_upload_file()
