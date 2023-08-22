import logging
from transform.manage_grade_transformation import MGT_CLASS_DICT
from transform.progression_grade_transformation import PGT_CLASS_DICT, ProgressionGradeBaseTransformation

# print("\n\n\n")
# print("-"*100)

file_id = 'PGLwATB'

print("\n\n\n")
print("-"*100)
try:
    psbuild_type = 'NewRecord'
    pg_nr_t = PGT_CLASS_DICT[psbuild_type](file_id=file_id)
    print(pg_nr_t.validated_input_df)
    pg_nr_t.start_transformation()
    print(pg_nr_t.output_df)
except Exception as e:
    logging.exception(e)

print("\n\n\n")
print("-"*100)
try:
    psbuild_type = 'ExistingRateChange'
    pg_erc_t = PGT_CLASS_DICT[psbuild_type](file_id=file_id)
    # print(pg_erc_t.validated_input_df)
    pg_erc_t.start_transformation()
    # print(pg_erc_t.output_df)
except Exception as e:
    logging.exception(e)

print("\n\n\n")
print("-"*100)
try:
    file_id = 'PGLwoATB'
    psbuild_type = 'NewRecord'
    pg_nr_t = PGT_CLASS_DICT[psbuild_type](file_id=file_id)
    print(pg_nr_t.validated_input_df)
    pg_nr_t.start_transformation()
    print(pg_nr_t.output_df)
except Exception as e:
    logging.exception(e)

print("\n\n\n")
print("-"*100)
try:
    file_id = 'PGLwoATB'
    psbuild_type = 'ExistingRateChange'
    pg_erc_t = PGT_CLASS_DICT[psbuild_type](file_id=file_id)
    print(pg_erc_t.validated_input_df)
    pg_erc_t.start_transformation()
    # print(pg_erc_t.output_df)
except Exception as e:
    logging.exception(e)

# print("-"*100)
# try:
#     input_file_name = 'PS Build Without ATB.xlsx'
#     mg_wo_atb_t = MGT_CLASS_DICT['WithoutATB'](input_file_name=input_file_name) # Initialization also includes Validation
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     input_file_name = 'PS Build With ATB.xlsx'
#     mg_w_atb_t = MGT_CLASS_DICT['WithATB'](input_file_name=input_file_name) # Initialization also includes Validation
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     input_file_name = 'PS Build Existing ATB.xlsx'
#     mg_e_atb_t = MGT_CLASS_DICT['ExistingATB'](input_file_name=input_file_name) # Initialization also includes Validation
# except Exception as e:
#     logging.exception("Exception Occured")
    
# print("-"*100)
# try:
#     if(mg_wo_atb_t):
#         print(mg_wo_atb_t.get_grade_code_list())
#         mg_wo_atb_t.start_transformation() # Function call to start transformation
#         output_df = mg_wo_atb_t.output_df
#         print(output_df.dtypes)
#     else:
#         print("Dead Class")
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     if(mg_w_atb_t):
#         print(mg_w_atb_t.get_grade_code_list())
#         mg_w_atb_t.start_transformation() # Function call to start transformation
#         output_df = mg_w_atb_t.output_df
#         print(output_df.dtypes)
#     else:
#         print("Dead Class")
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     if(mg_e_atb_t):
#         print(mg_e_atb_t.get_grade_code_list())
#         mg_e_atb_t.start_transformation() # Function call to start transformation
#         output_df = mg_e_atb_t.output_df
#         print(output_df.dtypes)
#     else:
#         print("Dead Class")
# except Exception as e:
#     logging.exception("Exception Occured")

# from transform.progression_grade_transformation import PGT_CLASS_DICT
# print("-"*100)
# try:
#     input_file_name = 'PS Build With ATB.xlsx'
#     pg_wo_atb_t = PGT_CLASS_DICT['Existing'](input_file_name=input_file_name) # Initialization also includes Validation
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     input_file_name = 'PS Build Without ATB.xlsx'
#     pg_w_atb_t = PGT_CLASS_DICT['NewRecord'](input_file_name=input_file_name) # Initialization also includes Validation
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     if(pg_w_atb_t):
#         print(pg_w_atb_t.get_grade_code_list())
#         pg_w_atb_t.start_transformation() # Function call to start transformation
#         output_df = pg_w_atb_t.output_df
#         print(output_df.dtypes)
#     else:
#         print("Dead Class")
# except Exception as e:
#     logging.exception("Exception Occured")

# print("-"*100)
# try:
#     if(pg_wo_atb_t):
#         print(pg_wo_atb_t.get_grade_code_list())
#         pg_wo_atb_t.start_transformation() # Function call to start transformation
#         output_df = pg_wo_atb_t.output_df
#         print(output_df.dtypes)
#     else:
#         print("Dead Class")
# except Exception as e:
#     logging.exception("Exception Occured")