import openpyxl
from config import ExcelFilePath, Colors, CellValues

class OpenExcel:
    def __init__(self, path_excel):
        self.path_excel = path_excel

    def workbook(self):
        self.wb = openpyxl.load_workbook(self.path_excel)
        self.sheet = self.wb.active

    def get_cell_range(self, text):
            for row in self.sheet.iter_rows(min_row=1, min_col=1, max_row=30, max_col=26):
                for cell in row:
                    if cell.value == text:
                        cell_name = str(cell.column_letter) + str(cell.row)
                        return cell_name
                    
    def cell_values_between(self, range_left, range_right):
        list_val = []
        row_focus = self.sheet[range_left:range_right]
        for cell in row_focus:
            for x in cell:
                list_val.append(x.value)

        return list_val

    def number_of_columns(self, loader_list, cloud_list):
        if len(loader_list) == len(cloud_list):
            print(f"{Colors.GREEN}PASSED: No Changes In The Loader File{Colors.RESET}")
        else:
            print(f"LOADER: Focused header columns = {Colors.BLUE}{len(loader_list)}{Colors.RESET}")
            print(f"CLOUD BUILD: Focused header columns = {Colors.BLUE}{len(cloud_list)}{Colors.RESET}")
            print(f"{Colors.RED}FAILED: Loader File Changed {Colors.RESET}")
            
    def partial_match(self, loader_list, cloud_list):
        length_checker = []
        for i in loader_list:
            for j in cloud_list: 
                if (i in j) == True or (i in f"* {j}"):
                    print(f"{Colors.GREEN}MATCHED:{Colors.RESET} {i} {Colors.GREEN}={Colors.RESET} {j}")
                    length_checker.append(i)

        if len(length_checker) == len(loader_list):
            print((f"{Colors.GREEN}PASSED: No Changes In The Loader File{Colors.RESET}"))
        else:
            print(f"{Colors.RED}Loader File Columns Doesn't Matched Cloud Build {Colors.RESET}")
            print(f"{Colors.RED}FAILED: Loader File Changed {Colors.RESET}")


if __name__=="__main__":
    loader_file = OpenExcel(ExcelFilePath.test_existing_atb_loader)
    loader_file.workbook()
    min1 = loader_file.get_cell_range(CellValues.loader_eff_start)
    print(f"LOADER: Left side cell = {Colors.BLUE}{min1}{Colors.RESET}")
    max1 = loader_file.get_cell_range(CellValues.loader_atb_rate)
    print(f"LOADER: Right side cell = {Colors.BLUE}{max1}{Colors.RESET}")
    loader_list = (loader_file.cell_values_between(min1,max1))
    print(f"{Colors.MAGENTA}{loader_list}{Colors.RESET}")

    print('=====================================')
    cloud_file = OpenExcel(ExcelFilePath.test_existing_cloud)
    cloud_file.workbook()
    min2 = cloud_file.get_cell_range(CellValues.cloud_eff_start)
    print(f"CLOUD BUILD: Left side cell = {Colors.BLUE}{min2}{Colors.RESET}")
    max2 = cloud_file.get_cell_range(CellValues.cloud_atb_rate)
    print((f"CLOUD BUILD: Left side cell = {Colors.BLUE}{max2}{Colors.RESET}"))
    cloud_list = cloud_file.cell_values_between(min2,max2)
    print(f"{Colors.MAGENTA}{cloud_list}{Colors.RESET}")

    print('=====================================')
    loader_file.number_of_columns(loader_list,cloud_list)

    print('=====================================')
    loader_file.partial_match(loader_list, cloud_list)




