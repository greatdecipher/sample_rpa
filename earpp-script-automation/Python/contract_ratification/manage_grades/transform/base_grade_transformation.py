import pandas as pd
import pandera as pa

import os
import configparser
from pathlib import Path

import os
environment = os.environ.get('ENV', 'DEV')

import logging
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

from pandera.typing import DataFrame, Series
from datetime import timedelta
from typing import final, List

from exceptions.exceptions import NoValidRowException, MissingColumnException

class BaseGradeTransformation():
    from models.base_models import PeopleSoftBuildModel as input_model
    from models.base_models import CloudBuildModel as output_model

    def __init__(self, 
            use_case: str,
            build_type: str,
            file_id: str, 
            isin_flag: bool,
            comments_filter_list: List
        ) -> None:

        self.use_case = use_case
        self.build_type = build_type
        self.file_id = file_id
        self.__isin_flag = isin_flag
        self.__comments_filter_list = comments_filter_list
        
        self.root_folder = None
        self.process_folder = None
        self.output_folder = None

        self.input_file_path = None
        self.output_file_path = None
        self.error_report_file_path = None
        
        self.input_df = None
        self.validated_input_df = None
        self.output_df = None

        self.error_remark = None
        
        self.__is_valid = False
        self.is_all_new_records = False
        self.__oracle_max_date = None 
        
        self.__set_paths()
        self.__set_input()
        
    def __set_paths(self):
        config = configparser.ConfigParser()
        config_file_path = os.path.join(str(Path(__file__).parents[1]),'manage_grades.ini')
        config.read(config_file_path)
        self.__oracle_max_date = str(config.get(f"{environment}.{self.use_case}", 'oraclemaxdate'))
        
        self.root_folder = Path(__file__).parents[1]
        self.process_folder = str(config.get(f"{environment}.{self.use_case}", 'processfolder' + self.build_type))
        self.output_folder = str(config.get(f"{environment}.{self.use_case}", 'outfolder' + self.build_type))

        self.input_file_path = f'{self.process_folder}/PSBuild_{self.file_id}.xlsx'
        self.output_file_path = f'{self.process_folder}/CBuild_{self.file_id}.xlsx'
        self.error_report_file_path = f'{self.process_folder}/PSBuild_Error_{self.file_id}.xlsx'
        
    def __set_input(self):
        try:
            self.input_df = self.__get_input_df()
            self.validated_input_df = self.__get_valid_input_df()
            self.__is_valid = True
            logging.info("Input DF read and validated successfully.")
        except FileNotFoundError as fnfe:
            self.__is_valid = False
            logging.exception("Input DF not Initialized due to FileNotFoundError",  exc_info=False)
            self.error_remark = "Input File not Found"
        except NoValidRowException as nvre:
            self.__is_valid = False
            logging.exception(nvre.message, exc_info=False)
            self.error_remark = nvre.message
        except MissingColumnException as mce:
            self.__is_valid = False
            logging.exception(mce.message, exc_info=False)
            self.error_remark = mce.message
        except pd.errors.UndefinedVariableError as uve:
            self.__if_valid = False
            logging.exception(uve, exc_info=False)
            self.error_remark = uve
        
    def __bool__(self):
        if self.__is_valid is False:
            logging.info("Returning False. No Input DF was initialized.")

        return self.__is_valid

    def __get_valid_input_df(self) -> DataFrame:
        """ Returns the filtered and validated input df

        """
        input_df = self.input_df
        isin_flag = self.__isin_flag
        comments_filter_list = self.__comments_filter_list

        if input_df.empty:
            logging.error("No Input DF initialized. Review Input File Path of Transformation Class")
            raise NoValidRowException("No Input DF initialized. Review Input File Path of Transformation Class")

        empty_rows_count = input_df.isna().all(axis=1).sum()
        if(empty_rows_count > 0):
            logging.info(f"Found {empty_rows_count} empty rows. Removing them from dataset.")
        input_df = input_df.replace("EOF", pd.NA)
        input_df = input_df.dropna(how='all')

        try:
            self.input_model.validate(input_df)
        except Exception as e:
            pass

        input_model_keys = list(self.input_model.__fields__.keys())
        matched_column_name = [column_name for column_name in input_model_keys if column_name not in input_df.columns.values]
        if len(matched_column_name) > 0:
            raise MissingColumnException(missing_columns_list=matched_column_name)

        # if 'Comments' not in input_df.columns.values:
        #     raise MissingColumnException(missing_columns_list=['Comments'])
        
        lower_comments_filter_list = [comments_filter.casefold() for comments_filter in comments_filter_list]
        if isin_flag:
            filtered_input_df = input_df.query("Comments.str.lower() in @lower_comments_filter_list")
        else:
            filtered_input_df = input_df.query("Comments.str.lower() not in @lower_comments_filter_list")

        if filtered_input_df.empty:
            raise NoValidRowException("No Rows Found with Comments: " + ', '.join(comments_filter_list))
        
        

        validated_input_df = self._get_validated_df(self.input_model, filtered_input_df)

        if validated_input_df.empty:
            raise NoValidRowException()

        
        self.is_all_new_records = (validated_input_df['Comments'] == 'New Record').all()
        if (self.is_all_new_records):
            print("All new records")

        validated_input_df.loc[:, ['Grade Code']] = validated_input_df[self.input_model.salary_administration_plan] + "-" + validated_input_df[self.input_model.salary_grade]
        return validated_input_df

    def __get_input_df(self) -> DataFrame:
        """ Returns the input_df from the initialized input file path

        """
        input_file_path = self.input_file_path
        input_df = pd.read_excel(input_file_path, dtype=str)
        
        logging.info("Read Input Data Successfully from file path: " + input_file_path)
        return input_df
    
    def start_transformation(self) -> None:
        """ Starts the transformation process from validated input df to saving the result as Excel File

        """
        logging.info("Transform PS Build to Cloud Build")
        validated_input_df = self.validated_input_df

        output_df = self._transform_input_to_output(validated_input_df)
        filtered_output_df = output_df[list(self.output_model.__fields__.keys())]

        output_file_path = self.output_file_path
        self._export_dataframe_as_excel(filtered_output_df, output_file_path)
        self.output_df = filtered_output_df
        logging.info("Excel Transformation Finished. Review output here:\n\t" + output_file_path)

    def get_grade_code_list(self):
        return self.validated_input_df['Grade Code'].unique().tolist()

    def get_sal_data(self):
        return self.validated_input_df.groupby(by=[self.input_model.salary_administration_plan, self.input_model.salary_grade]).size().reset_index()

    @final
    def _get_validated_df(self, schema_model, df_to_validate):
        """Returns filtered valid rows as dataframes.

        :param df_model: instance of DataFrameModel class that defines the columns of the dataframe for validation
        :param df_to_validate: instance of Dataframe that has the data that needs validation based on the DataFrameModel

        """
        logging.info("Validating Input File")
        error_report_file_path = self.error_report_file_path
        try:
            schema_model.validate(df_to_validate, lazy=True)
        except pa.errors.SchemaErrors as err:
            mismatch_atb_effdt_query = "~(check == 'check_atb_eff_dt_mismatch' & column != 'ATB Effdt**')"
            error_report_df = err.failure_cases.query(mismatch_atb_effdt_query)

            missing_columns_query = "schema_context == 'DataFrameSchema' & check == 'column_in_dataframe'"
            schema_error_df = error_report_df.query(missing_columns_query)
            missing_columns_list = schema_error_df['failure_case'].tolist()

            for column_name in missing_columns_list:
                col_position = len(df_to_validate.columns)
                df_to_validate.insert(col_position, column_name, "!! ERROR !! COLUMN NOT FOUND IN DATAFRAME !! ERROR !!")

            error_columns_query = "schema_context == 'Column' | check == 'check_atb_eff_dt_mismatch'"
            column_error_df = error_report_df.query(error_columns_query)

            if column_error_df.empty:
                self._export_dataframe_as_excel(df_to_validate, error_report_file_path)
                raise MissingColumnException(missing_columns_list=missing_columns_list) 

            column_error_df.insert(0, "Failed Checks", column_error_df[['column', 'check']].agg(': '.join, axis=1))
            column_error_df = column_error_df.groupby(['index']).agg({'Failed Checks': ', '.join})
            df_to_validate_with_error_column = df_to_validate.join(column_error_df)

            invalid_rows_per_grade_code_df = (df_to_validate_with_error_column.fillna('tmp_error')
                                    .groupby(by=[self.input_model.salary_administration_plan, self.input_model.salary_grade], as_index=False)
                                    .filter(lambda x: x['Failed Checks'].count() > 0)
                                    .replace('tmp_error', pd.NA)
                                )

            custom_error_message = {'str_length': 'character length mismatch', 'coerce_dtype': 'data type mismatch', 'not_nullable': 'Empty Cell', 'check_atb_eff_dt_mismatch': 'ATB Effdt Mismatch with Effective Date'}
            invalid_rows_per_grade_code_df['Failed Checks'] = invalid_rows_per_grade_code_df['Failed Checks'].replace(custom_error_message, regex=True)
            invalid_rows_per_grade_code_df['Failed Checks'] = invalid_rows_per_grade_code_df['Failed Checks'].fillna("Grade Code Group " + invalid_rows_per_grade_code_df[self.input_model.salary_administration_plan] + "-" + invalid_rows_per_grade_code_df[self.input_model.salary_grade] + " contains invalid row(s).")

            valid_rows_per_grade_code_df = (df_to_validate_with_error_column
                                    .groupby(by=[self.input_model.salary_administration_plan, self.input_model.salary_grade], as_index=False)
                                    .filter(lambda x: x['Failed Checks'].count() == 0)
                                )

            df_to_validate = valid_rows_per_grade_code_df
            invalid_rows_per_grade_code_df.insert(0, "row_no", invalid_rows_per_grade_code_df.index + 2)
            self._export_dataframe_as_excel(invalid_rows_per_grade_code_df, error_report_file_path)
            
            logging.error("Error rows found.")
            logging.info('\n\t'+ error_report_df.to_string().replace('\n', '\n\t')) 
            logging.error("Full Error Report found in:\n\t" + error_report_file_path)

            if len(missing_columns_list) > 0:
                raise MissingColumnException(missing_columns_list=missing_columns_list)

        filtered_cols_valid_rows_df = df_to_validate[list(schema_model.__fields__.keys())]
        return filtered_cols_valid_rows_df

    @final
    def _export_dataframe_as_excel(self, param_df: DataFrame, export_file_path: str) -> None:
        """Exports dataframe as an excel file.

        :param param_df: 

        """
        logging.info(f"Exporting Excel File to {export_file_path}")

        writer = pd.ExcelWriter(
            export_file_path,
            engine="xlsxwriter",
            datetime_format="MM/DD/YYYY",
            mode='w',
        )
        
        sheet_name = "Sheet1"

        param_df.to_excel(writer, sheet_name=sheet_name, index=False)
        for idx, column in enumerate(param_df):
            column_length = len(column)
            if param_df.empty: 
                max_data_length = 0
            else:
                max_data_length = param_df[column].astype(str).map(len).max() + 1
            column_length = max(max_data_length, column_length) + 2
            writer.sheets[sheet_name].set_column(idx, idx, column_length)

        writer.close()

    ### 
    ### OVERRIDE THESE FUNCTIONS FOR CHILD CLASSES AND IMPLEMENT CUSTOM TRANSFORMATIONS and CALCUlATIONS
    ###

    @pa.check_types
    def _transform_input_to_output(self, param_psb_df: DataFrame[input_model]) -> DataFrame[output_model]:
        """Returns a transformed Dataframe with field validation and data type coercion applied.

        :param param_psb_to_cb: instance of DataFrameModel class that defines the columns of the dataframe for validation

        """
        output_cb_df = param_psb_df.rename(columns={self.input_model.effective_date: self.output_model.effective_start_date})

        return output_cb_df

    ###
    ###  BASE CLASS FUNCTIONS FOR DATAFRAME COLUMNS CALCULATIONS AND MANIPULATIONS
    ###

    @final
    def _get_effective_end_date_column_per_grade(self, param_df: DataFrame) -> Series[pa.DateTime]:
        """Returns a Series Instance of calculated Effective End Dates per Grade Code based on the rule set in A1007 Document

        :param param_df: instance of DataFrame that contains Effective Start Date Column and Grade Code

        """
        return (param_df
                    .groupby(by=[self.output_model.grade_code], as_index=False)
                    .apply(lambda x: self._get_effective_end_date_column(x[self.output_model.effective_start_date]))
                    .droplevel(0)
                )

    @final
    def _get_ceiling_step_per_grade(self, param_df: DataFrame) -> Series[str]:
        """Returns a Series Instance of calculated Ceiling Step per Grade Code based on the rule set in A1006 Document

        :param param_df: instance of DataFrame that contains Grade Code and Step Number

        """
        param_df["Ceiling Step"] = "No"
        max_grade_code_indices = param_df.groupby([self.output_model.grade_code])[self.output_model.step_number].idxmax()
        cbt_wo_atb_df_merged = param_df.reset_index().merge(param_df.loc[max_grade_code_indices], on=[self.output_model.grade_code, self.output_model.step_number], how="left", indicator=True).set_index('index')
        all_max_grade_code_indices = cbt_wo_atb_df_merged[cbt_wo_atb_df_merged["_merge"] == "both"].index
        param_df.loc[all_max_grade_code_indices, "Ceiling Step"] = "Yes"
        return param_df[self.output_model.ceiling_step]

    @final
    def _get_calculated_sequence_column(self, param_df: DataFrame, last_identified_sequence) -> Series[int]:
        """Returns a Series Instance of Sequence calculated to iterate 1 in each Grade Code

        :param param_df: instance of DataFrame that contains Grade Code column
        :param last_identified_sequence: an integer which is one step higher than the last sequence in the source progress grade ladder details file

        """
        return (param_df
                    .sort_values(by=[self.output_model.grade_code])
                    .groupby(by=[self.output_model.grade_code])
                    .ngroup() + last_identified_sequence
                )

    def _get_merged_csg_report(self, param_df):
        """Returns a DataFrame from merging param_df and csg_report_df 

        :param param_df: instance of DataFrame that contains Effective Start Date and Grade Code column
        :param csg_report_file_name: filename of the csg report that contains external information

        """
        logging.info("Getting Data from CSG Report")
        csg_report_file_path = f'{self.process_folder}/CSGRepL_{self.file_id}.xlsx'

        if(self.is_all_new_records & ~os.path.exists(csg_report_file_path)):
            print("All new records")
            return None

        csg_report_df = pd.read_excel(csg_report_file_path, dtype=str)
        csg_report_df['Sequence Number'] = csg_report_df['Sequence Number'].astype('Int64')
    
        max_sequence_no_df = csg_report_df.groupby('Grade Code', as_index=False)['Sequence Number'].max()

        filtered_param_df = param_df.loc[:, ['Effective Start Date', 'Grade Code']]
        filtered_param_df['Effective Start Date'] = param_df['Effective Start Date'].dt.strftime('%m/%d/%Y')
        
        merged_df = filtered_param_df.merge(csg_report_df, how="left", on=['Effective Start Date', 'Grade Code'], indicator=True)
        if (len(param_df.index) < len(merged_df.index)):
            merged_df = merged_df.drop_duplicates()
        
        merged_df = merged_df.set_index(param_df.index)
        merged_df = merged_df.replace(self.__oracle_max_date, '')
        merged_df['Max Sequence Number'] = merged_df['Grade Code'].map(max_sequence_no_df.set_index('Grade Code')['Sequence Number']) + 1
        print(self.build_type + ": Extracted CSG Report Data")
        return merged_df

    def _get_spcl_accum_column(self, param_df) -> Series[str]:
        """Returns a DataFrame from merging param_df and sal plan report 

        :param param_df: instance of DataFrame that contains Salary Administration Plan

        """
        logging.info("Getting Data from Sal Plan")
        ps_report_file_path = f'{self.process_folder}/PSSalAcc_{self.file_id}.xlsx'
        ps_report_df = pd.read_excel(ps_report_file_path, sheet_name='sheet1', dtype=str, header=1)
        ps_report_df = ps_report_df.loc[:, ['Sal Plan', 'Spcl Accum']].drop_duplicates().fillna('')
        merged_df = param_df.merge(ps_report_df, how='left', left_on="*Salary Administration Plan", right_on="Sal Plan").set_index(param_df.index)
        return merged_df['Spcl Accum']

    @final
    @staticmethod
    def _get_effective_end_date_column(effective_start_date_column: Series[pa.DateTime]) -> Series[pa.DateTime]:
        """Returns a Series Instance of Effective End Date data derived from the passed effective_start_date_column parameter with the original indices

        :param effective_start_date_column: instance of Series containing the Effective Start Date data

        """
        dates_list = effective_start_date_column.unique()
        sorted_dates_list = sorted(dates_list)
        end_dates_dict = {}

        for index, date in enumerate(sorted_dates_list):
            if(index == len(sorted_dates_list) - 1):
                end_dates_dict[date] = pd.NaT
                break

            end_dates_dict[date] = sorted_dates_list[index + 1] - timedelta(days=1)

        effective_end_date_column = effective_start_date_column.map(end_dates_dict)

        return effective_end_date_column