import xlwings as xw
import pandas as pd
import openpyxl

from loader.config import Colors, base_loaders, column_names_coordinates, column_mapping_dict, ManageFilePath
import time
import os
import configparser
from pathlib import Path
environment = os.environ.get('ENV', 'DEV')

import sys
import subprocess
import logging
logging.basicConfig(filename = ManageFilePath.loader_log_path, filemode = 'w', format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

class LoaderProcessor():
    def __init__(self, psbuild_type, file_id):
        config = configparser.ConfigParser()
        config_file_path = os.path.join(str(Path(__file__).parents[1]),'manage_grades.ini')
        config.read(config_file_path)
        
        self.psbuild_type = psbuild_type
        
        root_folder = str(Path(__file__).parents[1])
        process_folder = str(config.get(f"{environment}.ManageGrades", 'processfolder' + psbuild_type))
        
        self.path_excel = f'{process_folder}/Loader_{file_id}.xlsx'
        self.cloud_loader_path = f"{root_folder}/{base_loaders[psbuild_type]}"
        self.cloud_build_path = f'{process_folder}/CBuild_{file_id}.xlsx'

        table = str.maketrans("[]", "()")
        book_key = f'Loader_{file_id}.xlsx'.translate(table)
        self.wb = wb = xw.apps[xw.apps.keys()[0]].books[book_key]
        self.sheet = wb.sheets.active
    
    def validate_loader_file(self):
        logging.info("Validating Loader File Columns")
        column_names_range = column_names_coordinates[self.psbuild_type]
        new_loader_column_names = self.sheet[column_names_range].value
    
        base_loader_sheet = openpyxl.load_workbook(self.cloud_loader_path, read_only=True).active
        base_loader_column_names = [col_name.value for col_name in list(base_loader_sheet[column_names_range][0])]
        
        
        logging.info(f"{Colors.GREEN}{len(new_loader_column_names)}/{len(base_loader_column_names)} Matches{Colors.RESET}")

        if base_loader_column_names == new_loader_column_names:
            logging.info((f"{Colors.GREEN}PASSED: No Changes In The Loader File{Colors.RESET}"))
            return True
        
        logging.info(f"{Colors.RED}Loader File Columns Don't Matched Cloud Build {Colors.RESET}")
        logging.info(f"{Colors.RED}FAILED: Need to update MyACI Loader file {Colors.RESET}")
        return False

    def copy_cb_to_loader(self):
        logging.info("Copying Cloud Build data to Loader file")
        column_names_range = column_names_coordinates[self.psbuild_type]
        loader_col_names = self.sheet[column_names_range]
        
        cbuild_df = pd.read_excel(self.cloud_build_path, dtype=str)
        cbuild_col_names = cbuild_df.columns.to_list()
        logging.info(cbuild_col_names)

        logging.info(loader_col_names.value)
        matched_columns_name = [column_mapping_dict[self.psbuild_type][loader_col_name.value] for loader_col_name in loader_col_names]
        cbuild_df = cbuild_df.reindex(matched_columns_name, axis=1)
        logging.info(cbuild_df.columns.values)

        cbuild_df_len = len(cbuild_df.index)
        first_data_row_cell = (loader_col_names.row + 1, loader_col_names.column)
        last_data_row_cell = (loader_col_names.row + 1 + cbuild_df_len, loader_col_names.last_cell.column)
        logging.info(first_data_row_cell, last_data_row_cell)
        logging.info(f"{loader_col_names.row + 1}:{loader_col_names.row + 1 + cbuild_df_len}")

        try:
            self.sheet.range(first_data_row_cell, last_data_row_cell).value = cbuild_df.values.tolist()
        except Exception as e:
            self.sheet.range(first_data_row_cell, last_data_row_cell).value = cbuild_df.values.tolist()

    def find_target_value_position_in_column(self, column_letter, search_string):
        last_row = self.sheet.range(column_letter + str(self.sheet.cells.last_cell.row)).end('up').row
        for row in range(1, last_row + 1):
            cell_value = self.sheet.cells(row, column_letter).value
            if cell_value == search_string:
                return row + 1 

    def are_cells_in_column_contain_this_str(self, column_letter, start_row, str_to_search):
        last_row = self.sheet.range(column_letter + str(self.sheet.cells.last_cell.row)).end('up').row
        for row in range(start_row, last_row + 1):
            cell_value = self.sheet.cells(row, column_letter).value
            if cell_value == str_to_search:
                return True
            
        return False

    def status_checker(self, loader_automator):
        column_letter = 'D'
        str_to_search = 'Upload Progress'
        start_index = self.find_target_value_position_in_column(column_letter, str_to_search)

        while self.are_cells_in_column_contain_this_str(column_letter, start_index, 'In Progress') or self.are_cells_in_column_contain_this_str(column_letter, start_index, 'Ready to Process'):
            loader_automator.refresh_upload()
            logging.info('Waiting for Upload Progress to complete...')

        logging.info("Succesfully Uploaded")