from base_grade_transformation import BaseGradeTransformation
from pathlib import Path
import pandas as pd
import pandera as pa

from typing import List
from pandera.typing import DataFrame

from models.progression_grade_models import use_case

import logging
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

class ProgressionGradeBaseTransformation(BaseGradeTransformation):
    def _get_csg_report(self):
        csg_report_file_path = f'{self.process_folder}/CSGRepL_{self.file_id}.xlsx'
        csg_report_df = pd.read_excel(csg_report_file_path, dtype=str)
        csg_report_df = csg_report_df.rename(columns=lambda column_name: column_name.strip())
        csg_report_df = csg_report_df.dropna(how='all')

        csg_report_df[self.input_model.labor_agreement] = csg_report_df[self.output_model.grade_ladder_name].str.split('_').str[0]
        return csg_report_df

    def _get_matched_cb_and_csg_df(self, output_df, csg_report_df, grade_ladder_details_column_list, merge_on_list):
        logging.info("Matching Cloud Build and CSG Report")
        filtered_csg_report_df = csg_report_df.loc[:, grade_ladder_details_column_list].drop_duplicates()
        # print("FILTERED DF")
        # print(filtered_csg_report_df)
        param_df = output_df.copy()
        param_df[self.output_model.effective_start_date] = param_df[self.output_model.effective_start_date].dt.strftime('%m/%d/%Y')
        merged_df = param_df.merge(filtered_csg_report_df, how="left", on=merge_on_list, indicator=True)
        merged_df = merged_df.set_index(output_df.index)
        # print("MERGED DF")
        # print(merged_df)
        return merged_df

    def _get_validated_output_df(self, output_df, csg_report_df, merge_on_list, keep_criteria):
        param_df = output_df.copy()
        grade_code_per_labor_agreement_df = param_df.loc[:, merge_on_list].drop_duplicates(keep="last")
        logging.info("Cloud Build: GRADE CODE PER LABOR AGREEMENT")
        print(grade_code_per_labor_agreement_df)

        grade_code_per_labor_agreement_df = csg_report_df.loc[:, merge_on_list].drop_duplicates(keep="last")
        logging.info("CSG Report: GRADE CODE PER LABOR AGREEMENT")
        print(grade_code_per_labor_agreement_df)

        param_df[self.output_model.effective_start_date] = param_df[self.output_model.effective_start_date].dt.strftime('%m/%d/%Y')
        merged_df = param_df.merge(grade_code_per_labor_agreement_df, how="left", on=merge_on_list, indicator=True)
        merged_df[self.output_model.effective_start_date] = pd.to_datetime(merged_df[self.output_model.effective_start_date], format='%m/%d/%Y')
        merged_df = merged_df.set_index(param_df.index)

        logging.info("Labeled Merge")
        print(merged_df)
        
        logging.info(f"Dropping Rows not {keep_criteria} and rest of rows with the same Grade Code")
        self.report_invalid_rows_df(merged_df)
    
        filter_lambda = lambda x_df: (x_df['_merge'] == keep_criteria).all()
        merged_df = merged_df.groupby([self.input_model.labor_agreement, self.output_model.grade_code]).filter(filter_lambda)
        merged_df = merged_df.drop(columns=['_merge'])

        print(merged_df)
        print("\n")
        return merged_df

    def report_invalid_rows_df(self, param_df):
        print(f"Reporting Invalid Rows for {self.build_type}")

        if self.build_type == 'ExistingRateChange':
            failed_check_message = "Existing Rate Change: Effective Start Date missing in CSG Report"
            keep_criteria = 'both'
        else:
            failed_check_message = "New Record: Grade Code already exists in CSG Report"
            keep_criteria = 'left_only'

        invalid_rows_df = param_df.query(f"`_merge` != '{keep_criteria}'")
        filter_lambda = lambda x_df: (x_df['_merge'] != keep_criteria).any()
        invalid_rows_per_grade_code_df = (param_df
                                .groupby([self.input_model.labor_agreement, self.output_model.grade_code])
                                .filter(filter_lambda)
                            )

        temp_input_df = self.input_df.copy()
        temp_input_df[self.output_model.grade_code] = temp_input_df[self.input_model.salary_administration_plan] + "-" + temp_input_df[self.input_model.salary_grade]
        invalid_rows_per_grade_code_df = temp_input_df.loc[invalid_rows_per_grade_code_df.index, :]
        invalid_rows_per_grade_code_df['Failed Checks'] = "Grade Code Group " + invalid_rows_per_grade_code_df[self.output_model.grade_code] + " contains invalid row(s)."

        invalid_rows_per_grade_code_df.loc[invalid_rows_df.index, ['Failed Checks']] = failed_check_message
        invalid_rows_per_grade_code_df.insert(0, "row_no", invalid_rows_per_grade_code_df.index + 2)
        invalid_rows_per_grade_code_df = invalid_rows_per_grade_code_df.drop(columns=[self.output_model.grade_code])

        try:
            prev_error_report_df = pd.read_excel(self.error_report_file_path, dtype=str)
            invalid_rows_per_grade_code_df = pd.concat([prev_error_report_df, invalid_rows_per_grade_code_df], ignore_index=True)
        except FileNotFoundError:
            print("No Previous Error File Found")
        finally:
            self._export_dataframe_as_excel(invalid_rows_per_grade_code_df, self.error_report_file_path)


class ProgressionGradeExistingTransformation(ProgressionGradeBaseTransformation):
    build_type = "ExistingRateChange"
    from models.progression_grade_models import PeopleSoftBuildModel as input_model
    from models.progression_grade_models import CloudBuildExistingRecordModel as output_model

    def __init__(self,
            file_id: str
        ) -> None:

        isin_flag = True
        comments_filter_list = ['Existing Rate Change']

        BaseGradeTransformation.__init__(self,
            use_case=use_case,
            build_type=self.build_type, 
            file_id=file_id, 
            isin_flag=isin_flag, 
            comments_filter_list=comments_filter_list
        )

        error_report_file_path_list = self.error_report_file_path.rsplit(".", 1)
        self.error_report_file_path = f"{error_report_file_path_list[0]}_{self.build_type}.{error_report_file_path_list[1]}"
        print(self.error_report_file_path)

        output_file_path_split_list = self.output_file_path.rsplit(".", 1)
        self.output_file_path = f"{output_file_path_split_list[0]}_{self.build_type}.{output_file_path_split_list[1]}"
        print(self.output_file_path)

    @pa.check_types
    def _transform_input_to_output(self, input_df: DataFrame[input_model]) -> DataFrame[output_model]:
        print("#"*50)
        logging.info(f"Transforming PS Build to {self.build_type} Cloud Build")
        input_model = self.input_model
        output_model = self.output_model

        output_df = input_df.rename(columns={input_model.effective_date: output_model.effective_start_date})
        output_df[output_model.grade_code] = output_df[input_model.salary_administration_plan] + "-" + output_df[input_model.salary_grade]
        
        labor_agreement_list = output_df[input_model.labor_agreement].unique()
        logging.info(f"Labor Agreements Found: {labor_agreement_list}")

        csg_report_df = self._get_csg_report()
        
        merge_on_list = [self.input_model.labor_agreement, self.output_model.grade_code, self.output_model.effective_start_date]
        keep_criteria = 'both'
        output_df = self._get_validated_output_df(output_df, csg_report_df, merge_on_list, keep_criteria)

        ################### Move 2 Function
        grade_ladder_details_column_list = [
            input_model.labor_agreement, 
            output_model.effective_start_date, 
            output_model.grade_code, 
            output_model.effective_end_date
        ]
        merge_on_list = [output_model.effective_start_date, input_model.labor_agreement, output_model.grade_code]
        merged_df = self._get_matched_cb_and_csg_df(output_df, csg_report_df, grade_ladder_details_column_list, merge_on_list)
        output_df[output_model.effective_end_date] = merged_df[output_model.effective_end_date]
        ###################

        ################### Move 2 Function
        grade_ladder_details_column_list = [
            input_model.labor_agreement, 
            output_model.effective_start_date, 
            output_model.grade_ladder_name
        ]
        merge_on_list = [output_model.effective_start_date, input_model.labor_agreement]
        merged_df = self._get_matched_cb_and_csg_df(output_df, csg_report_df, grade_ladder_details_column_list, merge_on_list)
        output_df[output_model.grade_ladder_name] = merged_df[output_model.grade_ladder_name]
        ###################

        next_sequence_number = csg_report_df[output_model.sequence].astype('Int64').max() + 1
        logging.info(f"Next Sequence Number: {next_sequence_number}")
        output_df[output_model.sequence] = self._get_calculated_sequence_column(output_df, next_sequence_number)        
        
        return output_df

    # def _get_validated_output_df(self, output_df, csg_report_df):
            ################## WRONG!
        # print("WRONG")
        # effective_start_date_list = csg_report_df[output_model.effective_start_date].unique().tolist()
        # unmatched_date_output_df = output_df.query(f'`{output_model.effective_start_date}` not in @effective_start_date_list')
        
        # if not unmatched_date_output_df.empty:
        #     logging.info("INVALID ROWS: EFFECTIVE START DATE NOT FOUND IN CSG REPORT")
        #     print(unmatched_date_output_df)

        # output_df = output_df.query(f'`{output_model.effective_start_date}` in @effective_start_date_list')
        # print("VALID ROWS: EFFECTIVE START DATE FOUND IN CSG REPORT")
        # print(output_df)
        ################## WRONG!

    #     param_df = output_df.copy()
    #     merge_on_list = [self.input_model.labor_agreement, self.output_model.grade_code, self.output_model.effective_start_date]
    #     grade_code_per_labor_agreement_df = param_df.loc[:, merge_on_list].drop_duplicates(keep="last")
    #     logging.info("Cloud Build: EFFECTIVE START DATE PER GRADE CODE PER LABOR AGREEMENT")
    #     print(grade_code_per_labor_agreement_df)

    #     merge_on_list = [self.input_model.labor_agreement, self.output_model.grade_code, self.output_model.effective_start_date]
    #     grade_code_per_labor_agreement_df = csg_report_df.loc[:, merge_on_list].drop_duplicates(keep="last")
    #     logging.info("CSG Report: EFFECTIVE START DATE PER GRADE CODE PER LABOR AGREEMENT")
    #     print(grade_code_per_labor_agreement_df)

    #     param_df[self.output_model.effective_start_date] = param_df[self.output_model.effective_start_date].dt.strftime('%m/%d/%Y')
    #     merged_df = param_df.merge(grade_code_per_labor_agreement_df, how="left", on=merge_on_list, indicator=True)
    #     merged_df[self.output_model.effective_start_date] = pd.to_datetime(merged_df[self.output_model.effective_start_date], format='%m/%d/%Y')
    #     merged_df = merged_df.set_index(output_df.index)
    #     logging.info("Labeled Merge")
    #     print(merged_df)
    #     logging.info("Dropping Rows With No Matching Effective Start Date in CSG Report")
    #     filter_lambda = lambda x_df: (x_df['_merge'] == 'both').all()
    #     merged_df = merged_df.groupby([self.input_model.labor_agreement, self.output_model.grade_code]).filter(filter_lambda)
    #     # print(merged_df)
    #     merged_df = merged_df.query(f"`_merge` == 'both'")
    #     merged_df = merged_df.drop(columns=['_merge'])
    #     # print("VALIDATED THROUGH MERGING")
    #     print(merged_df)
    #     print("\n")

        #Drop Grade Codes

        return merged_df

class ProgressionGradeNewRecordTransformation(ProgressionGradeBaseTransformation):
    build_type = "NewRecord"
    from models.progression_grade_models import PeopleSoftBuildModel as input_model
    from models.progression_grade_models import CloudBuildNewRecordModel as output_model
    
    def __init__(self,
            file_id: str
        ) -> None:

        isin_flag = True
        comments_filter_list = ['New Record']

        BaseGradeTransformation.__init__(self,
            use_case=use_case,
            build_type=self.build_type, 
            file_id=file_id, 
            isin_flag=isin_flag, 
            comments_filter_list=comments_filter_list
        )

        error_report_file_path_list = self.error_report_file_path.rsplit(".", 1)
        self.error_report_file_path = f"{error_report_file_path_list[0]}_{self.build_type}.{error_report_file_path_list[1]}"
        print(self.error_report_file_path)
        
        output_file_path_split_list = self.output_file_path.rsplit(".", 1)
        self.output_file_path = f"{output_file_path_split_list[0]}_{self.build_type}.{output_file_path_split_list[1]}"
        print(self.output_file_path)

    @pa.check_types
    def _transform_input_to_output(self, input_df: DataFrame[input_model]) -> DataFrame[output_model]:
        print("#"*50)
        logging.info(f"Transforming PS Build to {self.build_type} Cloud Build")
        input_model = self.input_model
        output_model = self.output_model

        output_df = input_df.rename(columns={input_model.effective_date: output_model.effective_start_date})
        output_df[output_model.grade_code] = output_df[input_model.salary_administration_plan] + "-" + output_df[input_model.salary_grade]
        output_df[output_model.effective_end_date] = self._get_effective_end_date_column_per_grade(output_df)
        output_df[output_model.status] = "Active"

        labor_agreement_list = output_df[input_model.labor_agreement].unique()
        logging.info(f"Labor Agreements Found: {labor_agreement_list}")

        csg_report_df = self._get_csg_report()

        merge_on_list = [input_model.labor_agreement, output_model.grade_code]
        keep_criteria = 'left_only'
        output_df = self._get_validated_output_df(output_df, csg_report_df, merge_on_list, keep_criteria)

        grade_ladder_details_column_list = [
            input_model.labor_agreement,
            output_model.grade_ladder_group,
            output_model.grade_ladder_name,
            output_model.grade_step_transaction_date,
            output_model.grade_step_transaction_date_formula_name,
            output_model.salary_update_transaction_date,
            output_model.rate_synchronization_transaction_date
        ]
        merge_on_list = [input_model.labor_agreement]
        merged_df = self._get_matched_cb_and_csg_df(output_df, csg_report_df, grade_ladder_details_column_list, merge_on_list)
        
        output_df[output_model.grade_ladder_group] = merged_df[output_model.grade_ladder_group]
        output_df[output_model.grade_ladder_name] = merged_df[output_model.grade_ladder_name]
        output_df[output_model.grade_step_transaction_date] = merged_df[output_model.grade_step_transaction_date]
        output_df[output_model.grade_step_transaction_date_formula_name] = output_df[output_model.grade_step_transaction_date].map(lambda x: "ABS_HYBRID_GRADE_LADDER_TRANSACTION_DATE" if x == 'Use Formula' else '')
        output_df[output_model.salary_update_transaction_date] = merged_df[output_model.salary_update_transaction_date]
        output_df[output_model.rate_synchronization_transaction_date] = merged_df[output_model.rate_synchronization_transaction_date]
        
        next_sequence_number = csg_report_df[output_model.sequence].astype('Int64').max() + 1
        logging.info(f"Next Sequence Number: {next_sequence_number}")
        output_df[output_model.sequence] = self._get_calculated_sequence_column(output_df, next_sequence_number)
        
        return output_df

    # def _get_validated_output_df(self, output_df, csg_report_df):
    #     ################### WRONG!
    #     # effective_start_date_list = csg_report_df[output_model.effective_start_date].unique().tolist()
    #     # unmatched_date_output_df = output_df.query(f'`{output_model.effective_start_date}` in @effective_start_date_list')
        
    #     # if not unmatched_date_output_df.empty:
    #     #     logging.info("INVALID ROWS: EFFECTIVE START DATE ALREADY FOUND IN CSG REPORT")
    #     #     print(unmatched_date_output_df)

    #     # output_df = output_df.query(f'`{output_model.effective_start_date}` not in @effective_start_date_list')
    #     # print("VALID ROWS: EFFECTIVE START DATE NOT FOUND IN CSG REPORT")
    #     # print(output_df)
    #     ################### WRONG!

        # merge_on_list = [self.input_model.labor_agreement, self.output_model.grade_code]
        # grade_code_per_labor_agreement_df = output_df.loc[:, merge_on_list].drop_duplicates(keep="last")
        # logging.info("Cloud Build: GRADE CODE PER LABOR AGREEMENT")
        # print(grade_code_per_labor_agreement_df)

        # merge_on_list = [self.input_model.labor_agreement, self.output_model.grade_code]
        # grade_code_per_labor_agreement_df = csg_report_df.loc[:, merge_on_list].drop_duplicates(keep="last")
        # logging.info("CSG Report: GRADE CODE PER LABOR AGREEMENT")
        # print(grade_code_per_labor_agreement_df)

        # merged_df = output_df.merge(grade_code_per_labor_agreement_df, how="left", on=merge_on_list, indicator=True)
        # merged_df = merged_df.set_index(output_df.index)

        # logging.info("Labeled Merge")
        # print(merged_df)
        # logging.info("Dropping Rows with Grade Codes Already Found in CSG Report")
        # filter_lambda = lambda x_df: (x_df['_merge'] == 'left_only').all()
        # merged_df = merged_df.groupby([self.input_model.labor_agreement, self.output_model.grade_code]).filter(filter_lambda)
        # # print(merged_df)
        # merged_df = merged_df.query(f"`_merge` == 'left_only'")
        # merged_df = merged_df.drop(columns=['_merge'])
        # # print("VALIDATED THROUGH MERGING")
        # print(merged_df)
        # print("\n")
        # return merged_df

PGT_CLASS_DICT = {
    ProgressionGradeExistingTransformation.build_type: ProgressionGradeExistingTransformation,
    ProgressionGradeNewRecordTransformation.build_type: ProgressionGradeNewRecordTransformation
}
    