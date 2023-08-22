from base_grade_transformation import BaseGradeTransformation
import pandas as pd
import pandera as pa

from typing import List
from pandera.typing import DataFrame

import logging 
logging.basicConfig(format='%(asctime)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S', level=logging.INFO)

from models.manage_grade_models import use_case

class ManageGradeWithoutAtbTransformation(BaseGradeTransformation):
    build_type = "WithoutATB"
    from models.manage_grade_models import PeopleSoftBuildWithoutAtbModel as input_model
    from models.manage_grade_models import CloudBuildWithoutAtbModel as output_model

    def __init__(self,
            file_id: str
        ) -> None:

        isin_flag = False
        comments_filter_list = ['No Update']

        super().__init__(
            use_case=use_case,
            build_type=self.build_type, 
            file_id=file_id, 
            isin_flag=isin_flag, 
            comments_filter_list=comments_filter_list
        )

    @pa.check_types
    def _transform_input_to_output(self, input_df: DataFrame[input_model]) -> DataFrame[output_model]:
        input_model = self.input_model
        output_model = self.output_model

        output_df = input_df.rename(columns={input_model.effective_date: output_model.effective_start_date})
        output_df[output_model.replace_first_effective_start_date] = ''
        output_df[output_model.set_code] = 'COMMON'
        output_df[output_model.grade_code] = output_df[input_model.salary_administration_plan] + "-" + output_df[input_model.salary_grade]
        output_df[output_model.effective_end_date] = self._get_effective_end_date_column_per_grade(output_df)
        output_df[output_model.grade_name] = 'WP-' + output_df[output_model.grade_code]
        output_df[output_model.active_status] = 'Active'
        output_df[output_model.step_number] = output_df[input_model.step]
        output_df[output_model.step_name] = "Step" + output_df[output_model.step_number].astype(str)
        output_df[output_model.ceiling_step] = self._get_ceiling_step_per_grade(output_df)
        output_df[output_model.progression_hours_accumulator] = self._get_spcl_accum_column(output_df)
    
        return output_df

class ManageGradeWithAtbTransformation(BaseGradeTransformation):
    build_type = "WithATB"
    from models.manage_grade_models import PeopleSoftBuildWithAtbModel as input_model
    from models.manage_grade_models import CloudBuildWithAtbModel as output_model

    def __init__(self,
            file_id: str
        ) -> None:

        isin_flag = False
        comments_filter_list: List = ['No Update']

        super().__init__(
            use_case=use_case,
            build_type=self.build_type, 
            file_id=file_id, 
            isin_flag=isin_flag, 
            comments_filter_list=comments_filter_list
        )

    @pa.check_types
    def _transform_input_to_output(self, input_df: DataFrame[input_model]) -> DataFrame[output_model]:
        input_model = self.input_model
        output_model = self.output_model

        output_df = input_df.rename(columns={input_model.effective_date: output_model.effective_start_date})
        output_df[output_model.replace_first_effective_start_date] = ''
        output_df[output_model.set_code] = 'COMMON'
        output_df[output_model.grade_code] = output_df[input_model.salary_administration_plan] + "-" + output_df[input_model.salary_grade]
        output_df[output_model.effective_end_date] = self._get_effective_end_date_column_per_grade(output_df)
        output_df[output_model.grade_name] = 'WP-' + output_df[output_model.grade_code]
        output_df[output_model.active_status] = 'Active'
        output_df[output_model.step_number] = output_df[input_model.step]
        output_df[output_model.step_name] = "Step" + output_df[output_model.step_number].astype(str)
        output_df[output_model.ceiling_step] = self._get_ceiling_step_per_grade(output_df)
        output_df[output_model.progression_hours_accumulator] = self._get_spcl_accum_column(output_df)
        output_df[output_model.information_category] = 'Across the Board Information'
        output_df[output_model.sequence_number] = output_df[input_model.sequence_number]
        output_df[output_model.eff_category_code] = 'Grade Legislation Data'
        output_df[output_model.atb_effective_date] = output_df[input_model.atb_effdt]
        output_df[output_model.atb_rate] = output_df[input_model.atb_flat_amt].apply(lambda x: '{:.6f}'.format(x))

        merged_df = self._get_merged_csg_report(output_df)

        if merged_df is not None:
            print("Appending CSG Data to Cloud Build")
            if output_df[output_model.sequence_number].isna().sum() > 0:
                print("Appending Missing Sequence Data to Cloud Build")
                output_df[output_model.sequence_number] = output_df[output_model.sequence_number].fillna(merged_df[output_model.sequence_number])
                output_df[output_model.sequence_number] = output_df[output_model.sequence_number].fillna(merged_df['Max Sequence Number'])
                output_df[output_model.sequence_number] = output_df[output_model.sequence_number].fillna(1)
            
            print("Appending Length of Service Data to Cloud Build") 
            output_df[output_model.atb_length_of_service_min_years] = merged_df[output_model.atb_length_of_service_min_years]
            output_df[output_model.atb_length_of_service_max_years] = merged_df[output_model.atb_length_of_service_max_years]
        else: 
            print("Proceeding with no CSG Data")
            output_df[output_model.sequence_number] = output_df[output_model.sequence_number].fillna(1)
            output_df[output_model.atb_length_of_service_min_years] = ''
            output_df[output_model.atb_length_of_service_max_years] = ''

        return output_df

class ManageGradeExistingTransformation(BaseGradeTransformation):
    build_type = "ExistingATB"
    from models.manage_grade_models import PeopleSoftBuildExistingAtbModel as input_model
    from models.manage_grade_models import CloudBuildExistingAtbModel as output_model

    def __init__(self, 
            file_id: str
        ) -> None:

        isin_flag = False
        comments_filter_list: List = ['No Update']

        super().__init__(
            use_case=use_case,
            build_type=self.build_type, 
            file_id=file_id, 
            isin_flag=isin_flag, 
            comments_filter_list=comments_filter_list
        )

    @pa.check_types
    def _transform_input_to_output(self, input_df: DataFrame[input_model]) -> DataFrame[output_model]:
        input_model = self.input_model
        output_model = self.output_model
        
        output_df = input_df.rename(columns={input_model.effective_date: output_model.effective_start_date})
        output_df[output_model.set_code] = 'COMMON'
        output_df[output_model.grade_code] = output_df[input_model.salary_administration_plan] + "-" + output_df[input_model.salary_grade]
        output_df[output_model.sequence_number] = output_df[input_model.sequence_number]
        output_df[output_model.eff_category_code] = 'Grade Legislation Data'
        output_df[output_model.atb_effective_date] = output_df[input_model.atb_effdt]
        output_df[output_model.atb_rate] = output_df[input_model.atb_flat_amt].apply(lambda x: '{:.6f}'.format(x))

        merged_df = self._get_merged_csg_report(output_df)
        output_df[output_model.effective_end_date] = merged_df[output_model.effective_end_date]
        output_df[output_model.grade_legislative_id] = merged_df[output_model.grade_legislative_id]

        return output_df

MGT_CLASS_DICT={
    ManageGradeWithoutAtbTransformation.build_type: ManageGradeWithoutAtbTransformation,
    ManageGradeWithAtbTransformation.build_type: ManageGradeWithAtbTransformation,
    ManageGradeExistingTransformation.build_type: ManageGradeExistingTransformation
    }