package com.albertsons.argus.dbcomparebatch.service.impl;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.albertsons.argus.domaindb.dto.DB2DTO;
import com.albertsons.argus.domaindb.dto.OracleDTO;
import com.albertsons.argus.domaindb.service.OracleService;
import com.albertsons.argus.dbcomparebatch.exception.DBCompareException;
import com.albertsons.argus.dbcomparebatch.service.DBCompareService;
import com.albertsons.argus.domaindb.exception.DB2ServiceException;
import com.albertsons.argus.domaindb.exception.OracleServiceException;
import com.albertsons.argus.domaindb.service.DB2Service;
import com.albertsons.argus.domain.util.AutomationUtil;
import com.albertsons.argus.mail.service.EmailService;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class DBCompareServiceImpl implements DBCompareService {
    private static final Logger LOG = LogManager.getLogger(DBCompareServiceImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DB2Service db2Service;

    @Autowired
    private OracleService oracleService;

    DecimalFormat df = new DecimalFormat("#.####");

    @Override
    public void getOracleAndDB2Rows(List<String> headers, List<OracleDTO> oracleDTOs, List<DB2DTO> db2DTOs) throws DBCompareException{
        LOG.log(Level.DEBUG, () -> "start getOracleAndDB2Rows method. . .");

        try {
            oracleDTOs = oracleService.getOracleDTO();
            db2DTOs = db2Service.getDB2DTO();

            List<String[]> rows = prepareRows(oracleDTOs, db2DTOs);

            if (!rows.isEmpty()) {
                sendDBCompareEmail(headers, rows);
            }


        } catch (OracleServiceException | DB2ServiceException e){
            LOG.log(Level.INFO, () -> "Problem getting Oracle and DB2 rows. . .");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end getOracleAndDB2Rows method. . .");

    }

    @Override
    public void sendDBCompareEmail(List<String> headers, List<String[]> rows) {
        LOG.log(Level.DEBUG, () -> "start sendDBCompareEmail method. . .");

        AutomationUtil util = new AutomationUtil();
        String filepath = environment.getProperty("mail.attachment.path") + environment.getProperty("mail.attachment");

        try {
            
            String body = toDBCompareHTMLString(headers, rows);
            writeToCSV(filepath, headers, rows);

            //NORMAL PRIORITY
            emailService.sendMessageWithAttachment(environment.getProperty("mail.dbcompare.from"),
                environment.getProperty("mail.dbcompare.from.alias"),
                environment.getProperty("mail.dbcompare.recipients", String[].class),
                environment.getProperty("mail.dbcompare.cc", String[].class),
                environment.getProperty("mail.dbcompare.subject") + " - "
                        + util.toDateString(new Date(), environment.getProperty("domain.util.date.format"), ""),
                body, filepath, NORMAL_PRIORITY, true);
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            deleteAttachment(filepath);
        }

        LOG.log(Level.DEBUG, () -> "end sendDBCompareEmail method. . .");

    }

    public String toDBCompareHTMLString(List<String> headers, List<String[]> rows) {        
        LOG.log(Level.DEBUG, () -> "start toDBCompareHTMLString method. . .");
        
        String HTML_TD_Red = "<td class=\"tg-hmp3\"><strong style=\"color: red;\">";
        String HTML_TD_STRONG = "</strong></td>";
        String HTML_TD_CLOSE = "</td>";
        String HTML_TD = "<td>";

        StringBuilder sb = new StringBuilder();
        StringBuilder sbTable = new StringBuilder();

        sb.append("<style type='text/css'>.tg  {border-collapse:collapse;border-color:#1a3146;border-spacing:0;}.tg "
                + "td{background-color:#EBF5FF;border-color:#1a3146;border-style:solid;border-width:1px;color:#444;  "
                + "font-family:Calibri, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + "th{background-color:#409cff;border-color:#1a3146;border-style:solid;border-width:1px;color:#fff;  font-family:Calibri, "
                + "sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}.tg "
                + ".tg-hmp3{background-color:#eaf2fe;text-align:left;vertical-align:top}.tg "
                + ".tg-hmp4{background-color:#FB4F4F;text-align:left;vertical-align:top}.tg "
                + ".tg-qiqm{background-color:#0071d2;text-align:center;vertical-align:top}</style>");

        sb.append("<body>");

        sb.append("Hi, <br><br>");
        
        sbTable.append("<table class=\"tg\"><thead><tr>");

        if (!rows.isEmpty()) {
            for (String header : headers) {
                sbTable.append("<th class=\"tg-qiqm\">");
                sbTable.append(header + "</th>");
                
            }

            sbTable.append("</tr></thead>");
            sbTable.append("<tbody>");
            
            for (int i = 0; i < rows.size(); i++){

                String[] data = rows.get(i);

                StringBuilder sbRow = new StringBuilder();
                
                sbRow.append("<tr>");

                for (int j = 0; j < data.length; j++){
                    if (j == (data.length - 2) && Double.parseDouble(data[j]) != 0){ // Variance with 0 not included
                        sbRow.append(HTML_TD_Red + data[j] + HTML_TD_STRONG + HTML_TD_Red + data[j+1] + "&#37;" + HTML_TD_STRONG);
                        sbRow.append("</tr>");
                        sbTable.append(sbRow);
                    }
                    else{
                        sbRow.append(HTML_TD + data[j] + HTML_TD_CLOSE);
                    }
                }
            }

            sbTable.append("</tbody>");
            sbTable.append("</table>");

            if (sbTable.toString().contains(HTML_TD_STRONG)){ // there is at least 1 row with a variance greater than 0
                sb.append("Showing tables with a variance greater than 0. This is arranged according to <b> variance percentage </b> in descending order. To view the complete list, please see the attachment. <br><br>");
            }
            else{
                sb.append("There are no tables with a variance greater than 0. Please see the attachment for the complete list. <br><br>");
            }


            sb.append(sbTable.toString());

            sb.append("<br><br> Thanks and Regards, <br>");
            sb.append("Bot");
            
            sb.append("</body>");
        }
        
        LOG.log(Level.DEBUG, () -> "end toDBCompareHTMLString method. . .");

        return sb.toString();
    }

    @Override
    public List<String[]> prepareRows(List<OracleDTO> oracleDTOs, List<DB2DTO> db2DTOs){       
        LOG.log(Level.DEBUG, () -> "start prepareRows method. . .");

        Double rowVariance = 0.0;
        Double total = 0.0;
        Double percentVariance = 0.0;
        List<String[]> rows = new ArrayList<>();

        try {

            if (!oracleDTOs.isEmpty() && !db2DTOs.isEmpty()) {
                for (int i = 0; i < oracleDTOs.size() && i < db2DTOs.size(); i++){ 

                    rowVariance = (double) oracleDTOs.get(i).getRowCount() - (double) db2DTOs.get(i).getRowCount();

                    if (rowVariance > 0){
                        total = (double) oracleDTOs.get(i).getRowCount();
                    }
                    else if (rowVariance < 0){
                        total = (double) db2DTOs.get(i).getRowCount();
                    }
                    
                    percentVariance = (Math.abs(rowVariance)/total) * 100;
                    Integer variance = (int) Math.round(rowVariance);

                    String rowResult[] = {  db2DTOs.get(i).getTableName(), 
                                            db2DTOs.get(i).getRowCount().toString(), 
                                            oracleDTOs.get(i).getTableName(), 
                                            oracleDTOs.get(i).getRowCount().toString(),
                                            variance.toString(),
                                            df.format(percentVariance)
                                        };
                    //add into list of rows
                    rows.add(rowResult);
                }

                String[][] listToArr = new String[rows.size()][rows.get(0).length];
                rows.toArray(listToArr);

                //sort according to the variance % (last column)
                List<String[]> sortedRows = sortByColumn(listToArr, (rows.get(0).length - 1));
                sortedRows = new ArrayList<String[]>(sortedRows);
                
                return sortedRows;
            }
            
            return rows; // return empty list
            
        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "Problem preparing rows. . .");
            LOG.error(e);
            return rows; // return empty list
        }
    }

    @Override
    public List<String[]> sortByColumn(String arr[][], int col){
        Arrays.sort(arr, new Comparator<String[]>() {
            @Override
            public int compare(final String[] entry1, final String[] entry2) {
                if (Double.parseDouble(entry1[col]) < Double.parseDouble(entry2[col]))
                    return 1;
                else
                    return -1;
            }
        });

        List<String[]> rows = Arrays.asList(arr);
        return rows;
    }

    @Override
    public void writeToCSV(String filepath, List<String> headers, List<String[]> rows){
        LOG.log(Level.DEBUG, () -> "start writeToCSV method. . .");

        try {
            PrintWriter pw = new PrintWriter(new File(filepath));

            StringBuffer sbHeader = new StringBuffer("");
            StringBuffer sbContent = new StringBuffer("");

            // write headers
            for (int i = 0; i < headers.size(); i++){
                sbHeader.append(headers.get(i));

                if (i < (headers.size() - 1)){
                    sbHeader.append(",");
                }
                else{
                    sbHeader.append("\n"); // last header
                }
            }

            pw.write(sbHeader.toString());

            // write full list of rows
            if (!rows.isEmpty()) {
                for (int i = 0; i < rows.size(); i++){

                    String[] data = rows.get(i);

                    for (int j = 0; j < data.length; j++){
                        if (j == (data.length - 1)){ // add % for last entry
                            sbContent.append(data[j] + "%");
                        }
                        else{
                            sbContent.append(data[j] + ",");
                        }
                    }
                    sbContent.append("\n");
                }

                pw.write(sbContent.toString());
            }

            pw.close();
        } catch (Exception e) {
            LOG.log(Level.INFO, () -> "Problem writing to CSV. . .");
            LOG.error(e);
        }
        
        LOG.log(Level.DEBUG, () -> "end writeToCSV method. . .");
    }

    @Override
    public void deleteAttachment(String filepath){
        LOG.log(Level.DEBUG, () -> "start deleteAttachment method. . .");

        File tempFile = new File(filepath);

        if (tempFile.exists()){
            tempFile.delete();
        }
        LOG.log(Level.DEBUG, () -> "end deleteAttachment method. . .");
    }

    @Override
    public List<String> getHeadersList() {
        LOG.log(Level.DEBUG, () -> "start getHeadersList method. . .");

        String[] getHeaderArr = environment.getProperty("mail.dbcompare.headers", String[].class);
        List<String> getHeadersList = Arrays.asList(getHeaderArr);

        LOG.log(Level.DEBUG, () -> "end getHeadersList method. . .");

        return getHeadersList;
    }

    @Override
    public void moveLogFile(){
        LOG.log(Level.DEBUG, () -> "start moveLogFile method. . .");

        try {
            Process vbscript = Runtime.getRuntime().exec("wscript " + environment.getProperty("automations.folder") + environment.getProperty("move.log.vbscript") + " " + environment.getProperty("automations.dbcompare.folder") + " " + environment.getProperty("log.file.name") + " " + environment.getProperty("log.file.name.extension") + " " + environment.getProperty("logging.folder"));
            vbscript.waitFor();

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "Failed to move log file to proper folder");
            LOG.error(e);
        }

        LOG.log(Level.DEBUG, () -> "end moveLogFile method. . .");
    }

}
