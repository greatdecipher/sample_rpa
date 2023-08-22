import pandas as pd
import shutil
from openpyxl import load_workbook

import os
import configparser
from pathlib import Path

class ExcelDataProcessor:
    def __init__(self, file_id, psbuild_type):
        config = configparser.ConfigParser()
        config_file_path = os.path.join(str(Path(__file__).parents[1]),'manage_grades.ini')
        config.read(config_file_path)
        
        process_folder = str(config.get(f"DEV.ManageGrades", 'processfolder' + psbuild_type))
        
        cbuild_filepath = f"{process_folder}/CBuild_{file_id}.xlsx"
        csgrep_filepath = f"{process_folder}/CSGRepV_{file_id}.xlsx"
        
        self.file1 = cbuild_filepath
        self.file2 = csgrep_filepath

    def get_common_columns(self):
        df1 = pd.read_excel(self.file1)
        df2 = pd.read_excel(self.file2)

        common_columns = sorted(list(set(df1.columns) & set(df2.columns)))
        return common_columns

    def read_excel_rows(self, file_path, columns):
        df = pd.read_excel(file_path, usecols=columns)
        df.fillna('', inplace=True)

        rows = []
        for index, row in df.iterrows():
            rows.append(row.tolist())

        return rows

    def search_rows_in_excel(self, file_path, rows, columns):
        df = pd.read_excel(file_path, usecols=columns)
        df.fillna('', inplace=True)

        date_columns = []
        for column in df.columns:
            if df[column].dtype == 'datetime64[ns]':
                date_columns.append(column)

        df = pd.read_excel(file_path, parse_dates=date_columns, usecols=columns)
        df.fillna('', inplace=True)

        Loading_df = []
        for index, row in df.iterrows():
            if row.tolist() in rows:
                #print("Row found:", row.tolist())
                Loading_df.append('SUCCESS')
            else:
                #print("Row not found:", row.tolist())
                Loading_df.append('FAIL')

        for column in date_columns:
            df[column] = df[column].dt.strftime('%m/%d/%Y')

        df['Loading'] = Loading_df

        return df

    def validate_cloud_build(self):
        common_columns = self.get_common_columns()

        rows = self.read_excel_rows(self.file1, common_columns)

        modified_df = self.search_rows_in_excel(self.file2, rows, common_columns)

        #modified_df.to_excel('merged_file.xlsx', index=False)

        # Save df to a new excel file, preserving the existing format        
        with pd.ExcelWriter('merged_file.xlsx', engine='openpyxl') as writer:
            print("cause of error")
            writer.book = load_workbook(self.file2)
            print("error")
            writer.sheets = dict((ws.title, ws) for ws in writer.book.worksheets)
            modified_df.to_excel(writer, index=False, sheet_name='Sheet1')

        shutil.move('merged_file.xlsx', self.file2)      

        #Return True if FAIL exists from any rows
        isfailed = ('FAIL' in modified_df['Loading'].values) 

        if isfailed:
            return False        
        else:
            return True        
