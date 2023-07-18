import openpyxl
from config import ExcelFilePath, Colors, CellValues, Labels
from excel_bot import OpenExcel
class TestAnyLoader:
    def open_loader():
        print('=======================================================')
        matched_loader_file = OpenExcel(ExcelFilePath.match_existing_loader,Labels.loader_existing_atb)
        matched_loader_file.workbook()
        matched_list = matched_loader_file.range_and_values(CellValues.loader_eff_start,
                        CellValues.loader_atb_rate, matched_loader_file)
        matched_loader_file.number_of_columns(matched_list,cloud_list)

        print('=======================================================')
        #exact match checker for matched loader files 'bound to pass'
        matched_loader_file.exact_match(matched_list,cloud_list)