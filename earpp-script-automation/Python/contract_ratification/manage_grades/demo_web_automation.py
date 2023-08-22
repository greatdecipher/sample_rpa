from web.manage_grade_automation import *

_file = "TEST_JASON00_1_MGWATB_20230720101010"
psbuild_type = "WithATB"
grade_codes = ['AC05-101', 'AC05-102', 'AC05-104', 'AC05-107', 'AC05-108',
                            'AC05-109', 'AC05-110', 'AC05-111', 'AC05-112', 'AC05-113']

#grade_codes = ['XYZ', 'ABC', 'JKL']
get_loader_file(psbuild_type=psbuild_type, file_id=_file)
get_csg_report(psbuild_type=psbuild_type, grade_codes=grade_codes, file_id=_file)
get_sal_plan_accum(psbuild_type=psbuild_type, file_id=_file)