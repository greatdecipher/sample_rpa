import pandera as pa

from typing import Optional
from pandera.typing import DataFrame, Series

"""
Template:
class {BuildType}Model(pa.DataFrameModel):
    column_name_in_snake_case: Series[{datatype of column}] = pa.Field(alias={Column Name to be displayed in Dataframe/Excel}, coerce=True, {insert other parameters such as nullable, inline-checks})

"""

grade_model_name = "Demo Grade"

class PeopleSoftBuildModel(pa.DataFrameModel):
    _alias = '' # add instructions for non field variables

    effective_date: Series[pa.DateTime] = pa.Field(alias="*Effective Date", coerce=True)
    salary_administration_plan: Series[str] = pa.Field(alias="*Salary Administration Plan", coerce=True)
    salary_grade: Series[str] = pa.Field(alias="*Salary Grade", coerce=True)
    comments: Series[str] = pa.Field(alias="Comments", eq="New Record", coerce=True)

class CloudBuildModel(pa.DataFrameModel):
    effective_start_date: Series[pa.DateTime] = pa.Field(alias="Effective Start Date", coerce=True)
    effective_end_date: Series[pa.DateTime] = pa.Field(alias="Effective End Date", nullable=True, coerce=True)