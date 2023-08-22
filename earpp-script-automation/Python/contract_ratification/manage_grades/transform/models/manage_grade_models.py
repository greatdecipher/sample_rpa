import pandera as pa
import pandas as pd

from pandera.typing import Series

use_case = "ManageGrades" # to note, not final name for the variable

class _PeopleSoftBuildModel(pa.DataFrameModel):
    effective_date: Series[pa.DateTime] = pa.Field(alias="*Effective Date", coerce=True)
    salary_administration_plan: Series[str] = pa.Field(alias="*Salary Administration Plan", str_length={"min_value":4, "max_value":4}, coerce=True)
    salary_grade: Series[str] = pa.Field(alias="*Salary Grade", coerce=True, str_length={"min_value":3,"max_value":3})
    comments: Series[str] = pa.Field(alias="Comments", coerce=True)

class PeopleSoftBuildWithoutAtbModel(_PeopleSoftBuildModel):
    _alias = "PS Build Without ATB"
    
    step: Series[int] = pa.Field(alias="*Step", coerce=True)

class _PeopleSoftBuildHasAtbModel(_PeopleSoftBuildModel):
    sequence_number: Series[pa.INT64] = pa.Field(alias="*Sequence", coerce=True, nullable=True)
    atb_effdt: Series[pa.DateTime] = pa.Field(alias="ATB Effdt**", coerce=True)
    atb_flat_amt: Series[float] = pa.Field(alias="ATB Flat Amt**", coerce=True)

    @pa.dataframe_check
    def check_atb_eff_dt_mismatch(cls, df: pd.DataFrame) -> Series[bool]:
        return df[cls.atb_effdt] == df[cls.effective_date]

class PeopleSoftBuildWithAtbModel(_PeopleSoftBuildHasAtbModel, PeopleSoftBuildWithoutAtbModel):
    _alias = "PS Build With ATB"

class PeopleSoftBuildExistingAtbModel(_PeopleSoftBuildHasAtbModel, _PeopleSoftBuildModel):
    _alias = "PS Build Existing ATB"

class _CloudBuildModel(pa.DataFrameModel):
    effective_start_date: Series[pa.DateTime] = pa.Field(alias="Effective Start Date", coerce=True)
    effective_end_date: Series[pa.DateTime] = pa.Field(alias="Effective End Date", nullable=True, coerce=True)
    set_code: Series[str] = pa.Field(alias="Set Code", eq="COMMON", coerce=True)
    grade_code: Series[str] = pa.Field(alias="Grade Code", coerce=True)

class CloudBuildWithoutAtbModel(_CloudBuildModel):
    _alias = "Cloud Build Without ATB"

    replace_first_effective_start_date: Series[str] = pa.Field(alias="Replace First Effective Start Date", nullable=True, coerce=True) # Ask if optional means nullable or optional to exist in file
    grade_name: Series[str] = pa.Field(alias="Grade Name", coerce=True)
    active_status: Series[str] = pa.Field(alias="Active Status", eq="Active", coerce=True)
    step_number: Series[int] = pa.Field(alias="Step Number", coerce=True)
    step_name: Series[str] = pa.Field(alias="Step Name", coerce=True)
    ceiling_step: Series[str] = pa.Field(alias="Ceiling Step", isin=["Yes", "No"], coerce=True)
    progression_hours_accumulator: Series[str] = pa.Field(alias="Progression Hours Accumulator", coerce=True)

class _CloudBuildHasAtbModel(pa.DataFrameModel):
    sequence_number: Series[pa.INT64] = pa.Field(alias="Sequence Number", coerce=True, nullable=True)
    eff_category_code: Series[str] = pa.Field(alias="EFF_CATEGORY_CODE", eq="Grade Legislation Data", coerce=True)
    atb_effective_date: Series[pa.DateTime] = pa.Field(alias="ATB Effective Date", coerce=True)
    atb_rate: Series[str] = pa.Field(alias="ATB Rate", coerce=True)

class CloudBuildExistingAtbModel(_CloudBuildHasAtbModel, _CloudBuildModel):
    _alias = "Cloud Build Existing ATB"
    grade_legislative_id: Series[str] = pa.Field(alias="Grade Legislative ID", coerce=True)
    
class CloudBuildWithAtbModel(_CloudBuildHasAtbModel, CloudBuildWithoutAtbModel):
    _alias = "Cloud Build With ATB"

    information_category: Series[str] = pa.Field(alias="Information Category", eq="Across the Board Information", coerce=True)
    atb_length_of_service_min_years: Series[str] = pa.Field(alias="ATB Length of Service (Min Years)", coerce=True, nullable=True)
    atb_length_of_service_max_years: Series[str] = pa.Field(alias="ATB Length of Service (Max Years)", coerce=True, nullable=True)