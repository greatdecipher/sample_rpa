class NoValidRowException(Exception):
    def __init__(self, message="No Valid Rows after Filter and Validation") -> None:
        self.message = message
        super().__init__(message)

class MissingColumnException(Exception):
    def __init__(self, message="Missing column(s) found while validating dataframe: ", missing_columns_list=[]) -> None:
        self.message = message + str(missing_columns_list)
        super().__init__(message + str(missing_columns_list))