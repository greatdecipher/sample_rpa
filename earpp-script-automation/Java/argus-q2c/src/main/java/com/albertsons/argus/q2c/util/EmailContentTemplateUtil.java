package com.albertsons.argus.q2c.util;

public class EmailContentTemplateUtil {
    // AR INVOICES Email Template
    public String noBatchFileContentArInv(String file, String dateSTr) {
        return "Hello,<br><br>No " + file + " file found in OIC within window " + dateSTr
                + "<br>This type of file is optional.<br><br>Thanks & Regards,<br>Bot";
    }

    public String cmmNotEqualContentArInv(String file, String dateSTr, int countProcessInstance, int countInterface) {
        return "Hello,<br><br>The cmm count is not equal for " + file + ".<br>Count in abs_process_instance_tbl: "
                + countProcessInstance + "<br>Count in abs_ra_interface_lines_all_tbl: " + countInterface
                + "<br><br>Thanks & Regards,<br>Bot";
    }

    public String notSucceededArInv(String jobId) {
        return "<body>Hello, <br><br>In AR Invoices, is not SUCCEEDED. See below:<br>Job ID: " + jobId
                + "<br><br><br>Thanks & Regards, <br>Bot</body>";
    }

    //

}
