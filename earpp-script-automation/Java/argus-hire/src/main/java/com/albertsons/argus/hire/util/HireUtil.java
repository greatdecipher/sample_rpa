package com.albertsons.argus.hire.util;

import java.util.List;

import com.albertsons.argus.hire.model.HireError;

public class HireUtil {

    private String STYLE_HTML_HIRE_CONST = "<style>#myTable{font-family:'helvetica neue',helvetica,arial,'lucida grande',sans-serif;border-collapse:collapse}#myTable td{border:solid 1px #c9c9c9;padding:10px;font-size:12px;text-align:center}#myTable tr{background-color:#ebebeb;color:#000}#myTable tr:hover{background-color:#fff;color:#000}#myTable th{border:solid 1px #bfbfbf;padding:10px;color:#000;background-color:#d4d4d4;text-align:center;font-size:12px}</style>";
    private String THEAD_HTML_HIRE_CONST = "<thead><tr><th scope=\"col\">Filename</th><th scope=\"col\">Scenario</th><th scope=\"col\">Employee Number</th><th scope=\"col\">Note</th></tr></thead>";
    private String CONTENT_BODY_HIRE_CONST = "Hello HR Data Integrity team, <br><br> Processing errors were encountered for this batch. Kindly correct the employees\u2019 hire date in the system: <br><br>";

    private String CONTENT_BODY_ACK_CONST = "Hello HR Data Integrity team, <br><br> The file is successfully processed. ";

    public String getHireErrorHTML(List<HireError> errorList) {

        StringBuilder hireContentHTMLSB = new StringBuilder(STYLE_HTML_HIRE_CONST);
        hireContentHTMLSB.append(CONTENT_BODY_HIRE_CONST);
        hireContentHTMLSB.append("<table id=\"myTable\" width=\"100%\">" + THEAD_HTML_HIRE_CONST + "<tbody>");

        errorList.stream().forEach(hireError -> {
            hireContentHTMLSB.append("<tr> ");
            hireContentHTMLSB.append("<td> " + hireError.getFileName() + " </td>");
            hireContentHTMLSB.append("<td> " + hireError.getScenario() + " </td>");
            hireContentHTMLSB.append("<td> " + hireError.getEmployeeNumber() + " </td>");
            hireContentHTMLSB.append("<td> "
                    + hireError.getErrorMessage().substring(0, Math.min(hireError.getErrorMessage().length(), 400))
                    + " </td>");
            hireContentHTMLSB.append(" </tr>");
        });

        hireContentHTMLSB.append("</tbody></table>");

        return hireContentHTMLSB.toString();

    }

    public String getAcknowledgeHTML(List<String> employeeNumberList) {
        StringBuilder hireContentHTMLSB = new StringBuilder(CONTENT_BODY_ACK_CONST);

        if (!employeeNumberList.isEmpty()) {
            hireContentHTMLSB.append("Kindly update the new hire/rehire dates in PeopleSoft for the listed employees below: <br>");
            hireContentHTMLSB.append("<ul>");
            employeeNumberList.stream().forEach(employee -> {
                hireContentHTMLSB.append("<li>" + employee + "</li>");
            });
            hireContentHTMLSB.append("</ul>");
        }else{
            hireContentHTMLSB.append("No successful processed employee in this file. Please kindly check the Status Report in the separate email.");
        }

        hireContentHTMLSB.append("<br><br>Thanks,<br>Bot");

        return hireContentHTMLSB.toString();


    }

}
