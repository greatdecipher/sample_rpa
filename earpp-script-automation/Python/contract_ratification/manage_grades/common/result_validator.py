import os
import configparser
from pathlib import Path
import pandas as pd

def validate_cbuild_csg(file_id, psbuild_type):
    config = configparser.ConfigParser()
    config_file_path = os.path.join(str(Path(__file__).parents[1]),'manage_grades.ini')
    config.read(config_file_path)
    
    process_folder = str(config.get(f"DEV.ManageGrades", 'processfolder' + psbuild_type))
    
    cbuild_filepath = f"{process_folder}/CBuild_{file_id}.xlsx"
    csgrep_filepath = f"{process_folder}/CSGRepV_{file_id}.xlsx"

    csgrep_df = pd.read_excel(csgrep_filepath, dtype=str, keep_default_na=False)
    og_cbuild_df = pd.read_excel(cbuild_filepath, dtype=str, keep_default_na=False)
    cbuild_df = og_cbuild_df.reindex(csgrep_df.columns, axis=1)
    csgrep_df = csgrep_df.fillna('')
    cbuild_df = cbuild_df.fillna('')
    
    oracle_max_date = str(config.get(f"DEV.ManageGrades", 'oraclemaxdate'))
    csgrep_df = csgrep_df.replace(oracle_max_date, '')
    
    for column_name in cbuild_df.columns:
        cbuild_df[column_name]
        if 'date' in column_name.lower():
            cbuild_df[column_name] = pd.to_datetime(cbuild_df[column_name]).dt.strftime(date_format="%m/%d/%Y").fillna('')

    merged_df = cbuild_df.merge(csgrep_df, how='left', indicator=True)
    merged_df['_merge'] = merged_df['_merge'].map(lambda x: "SUCCESS" if x == 'both' else 'FAILURE')
    merged_df = merged_df.rename(columns={'_merge':'Loading'})

    if 'Replace First Effective Start Date' in og_cbuild_df.columns.values:
        print("Returning Replace First Effective Start Date Column")
        merged_df.insert(0, 'Replace First Effective Start Date', og_cbuild_df['Replace First Effective Start Date'])
    
    print(merged_df.to_string())

    loading_values = merged_df['Loading'].values

    writer = pd.ExcelWriter(
            cbuild_filepath,
            engine="xlsxwriter",
            datetime_format="MM/DD/YYYY",
            mode='w',
        )
        
    sheet_name = "Sheet1"
    merged_df.to_excel(writer, sheet_name=sheet_name, index=False)

    for idx, column_name in enumerate(merged_df):
        column_length = max(merged_df[column_name].astype(str).map(len).max(), len(column_name)) + 2
        writer.sheets[sheet_name].set_column(idx, idx, column_length)

    writer.close()
    
    return (loading_values == 'SUCCESS').all()