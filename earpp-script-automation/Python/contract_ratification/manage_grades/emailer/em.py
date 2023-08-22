# Test script to call emailer function
import emailer

em = emailer.eMailer()

if __name__ == '__main__':
    # Params: Use Case, FileID, Status("Success" or "Error"), Remark or Short Desc of Error(Blank if Success)
    #em.emailer("Manage Grades", "CJAVI00_4_MGWATB_20230809122802","Success", "" )  
    em.emailer("Progression Grade Ladder", "CJAVI00_1_PGLWATB_20230807095139","Success", "" )