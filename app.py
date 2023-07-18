from excel_bot import OpenExcel
from config import ExcelFilePath

existing_atb = OpenExcel(ExcelFilePath.test_existing_atb_loader)
with_atb = OpenExcel(ExcelFilePath.test_with_atb_loader)
without_atb = OpenExcel(ExcelFilePath.test_without_atb_loader)


existing_atb.validate_loader_file()