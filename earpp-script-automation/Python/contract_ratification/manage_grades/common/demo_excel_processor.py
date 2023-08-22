from excel_processor import ExcelDataProcessor

processor = ExcelDataProcessor('file1.xlsx', 'file2.xlsx')
if processor.validate_cloud_build():
    print('Success')
else:
    print('Fail')
