from os.path import dirname, abspath


class Constants:

    # MANAGE GRADES
    MANAGE_GRADE_WITH_ATB = "CSG_Manage Grades with ATB"
    MANAGE_GRADE_WITHOUT_ATB = "CSG_Manage Grades without ATB"
    MANAGE_GRADE_EXISTING_ATB = "CSG_Manage Existing ATB"

    DOWNLOAD_PATH = dirname(dirname(abspath(__file__)))

    # PEOPLESOFT
    SALARY_PLAN_ACCUM = "CONTRACTS_LA_SAL_PLAN_ACCUM"

    # FOLDER FILE
    PROCESSING_TEMPLATE = "\\earpprpaqast01.file.core.windows.net\fileshare-rpa-coe\Argus\ContractRatification\ManageGrades"

    # REPORT TYPES
    LOADER_FILE = "Loader Report"
    CSG_FILE = "CSG Report"
    SAL_PLAN_FILE = "Salary Plan Accum Report"