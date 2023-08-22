import pandera as pa

from pandera.typing import Series

use_case  = "ProgressionGradeLadder"

class PeopleSoftBuildModel(pa.DataFrameModel):
    labor_agreement: Series[str] = pa.Field(alias="Labor Agreement", coerce=True)
    effective_date: Series[pa.DateTime] = pa.Field(alias="*Effective Date", coerce=True)
    salary_administration_plan: Series[str] = pa.Field(alias="*Salary Administration Plan", str_length={"min_value":4, "max_value":4}, coerce=True)
    salary_grade: Series[str] = pa.Field(alias="*Salary Grade", coerce=True, str_length={"min_value":3,"max_value":3})
    comments: Series[str] = pa.Field(alias="Comments", coerce=True)
    
class PeopleSoftBuildExistingAtbModel(PeopleSoftBuildModel):
    _alias = "PS Build Existing ATB"
    
class PeopleSoftBuildWithAtbModel(PeopleSoftBuildModel):
    _alias = "PS Build With ATB"

class CloudBuildExistingRecordModel(pa.DataFrameModel):
    _alias = "Cloud Build Existing Record"

    effective_start_date: Series[pa.DateTime] = pa.Field(alias="Effective Start Date", coerce=True)
    effective_end_date: Series[pa.DateTime] = pa.Field(alias="Effective End Date", nullable=True, coerce=True)
    grade_ladder_name: Series[str] = pa.Field(alias="Grade Ladder Name", coerce=True)
    sequence: Series[pa.INT64] = pa.Field(alias="Sequence", coerce=True, nullable=True)
    grade_code: Series[str] = pa.Field(alias="Grade Code", coerce=True)

class CloudBuildNewRecordModel(CloudBuildExistingRecordModel):
    _alias = "Cloud Build New Record"
    
    # effective_start_date: Series[pa.DateTime] = pa.Field(alias="Effective Start Date", coerce=True)
    # effective_end_date: Series[pa.DateTime] = pa.Field(alias="Effective End Date", nullable=True, coerce=True)
    status: Series[str] = pa.Field(alias="Status", coerce=True)
    # grade_code: Series[str] = pa.Field(alias="Grade Code", coerce=True)
    grade_ladder_group: Series[str] = pa.Field(alias="Grade Ladder Group", coerce=True)
    # grade_ladder_name: Series[str] = pa.Field(alias="Grade Ladder Name", coerce=True)
    grade_step_transaction_date: Series[str] = pa.Field(alias="Grade Step Transaction Date", coerce=True)
    grade_step_transaction_date_formula_name: Series[str] = pa.Field(alias="Grade Step Transaction Date Formula Name", coerce=True)
    salary_update_transaction_date: Series[str] = pa.Field(alias="Salary Update Transaction Date", coerce=True)
    rate_synchronization_transaction_date: Series[str] = pa.Field(alias="Rate Synchronization Transaction Date", coerce=True)
    # sequence: Series[int] = pa.Field(alias="Sequence", coerce=True)

