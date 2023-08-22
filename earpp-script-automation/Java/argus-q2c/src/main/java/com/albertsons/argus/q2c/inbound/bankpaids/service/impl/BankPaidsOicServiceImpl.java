package com.albertsons.argus.q2c.inbound.bankpaids.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.albertsons.argus.domaindbq2c.service.DbOracleService;
import com.albertsons.argus.domaindbq2c.dto.BankProcessInstanceDTO;
import com.albertsons.argus.domaindbq2c.dto.BankStatementsDTO;
import com.albertsons.argus.q2c.common.service.Q2CCommonService;
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsErpService;
import com.albertsons.argus.q2c.inbound.bankpaids.service.BankPaidsOicService;

@Service
public class BankPaidsOicServiceImpl implements BankPaidsOicService{
    private static final Logger LOG = LogManager.getLogger(BankPaidsOicServiceImpl.class);

    @Autowired
    Environment environment;

    @Autowired
    DbOracleService dbOracleService;
    
    @Autowired
    BankPaidsErpService bankPaidsErpService;

    @Autowired
    Q2CCommonService q2cCommonService;

    private String execTimestamp;

    @Override
    public void bankPaidsMain(String filename, String dateToCheck, String execTimestamp){
        LOG.log(Level.DEBUG, () -> "start bankPaidsMain method. . .");

        this.execTimestamp = execTimestamp;

        List<BankStatementsDTO> bankStatementsDTOs = checkBankStatements(filename, dateToCheck);

        //LOG.log(Level.DEBUG, () -> "bankStatementsDTOs size: " + bankStatementsDTOs.size());
        if (bankStatementsDTOs != null && bankStatementsDTOs.size() > 0){

            if (checkBankProcessInstance(filename)){
                bankPaidsErpService.bankPaidsErpReadBankMain(dateToCheck, bankStatementsDTOs, execTimestamp);
                bankPaidsErpService.bankPaidsErpMain(bankStatementsDTOs, execTimestamp);
            }

        }
        else{
            String body = "<body>Hello, <br><br>";
            String subject = environment.getProperty("inbound.bank.paids.query.1.failure.subject");

            if (filename.contains("EAS_CB_MO_")){
                body += "No results found after querying for altdocid " + filename + dateToCheck + "%" + " in Staging Table in Inbound EAS CB MO Validation process.";
                subject = subject.replace("Bank Paids", "EAS CB MO");
            }
            else{
                body += "No results found after querying for altdocid " + filename + dateToCheck + "%" + " in Staging Table in Inbound Bank Paids Validation process.";
            }
    
            body += "<br><br>" +
                    "Thanks & Regards, <br>" + 
                    "Bot</body>";

            q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                        environment.getProperty("mail.argus.cc", String[].class), 
                        subject, body, execTimestamp);
        }

        LOG.log(Level.DEBUG, () -> "end bankPaidsMain method. . .");
    }

    @Override
	public List<BankStatementsDTO> checkBankStatements(String filename, String dateToCheck){
        LOG.log(Level.DEBUG, () -> "start checkBankStatements method. . .");

        try {
            filename = filename + dateToCheck + "%"; //append date and modulo symbol to the filename to be used in query
           
            List<BankStatementsDTO> bankStatementsDTOs = new ArrayList<>();
            bankStatementsDTOs = dbOracleService.getBankStatementsDTOs(filename);

            if (bankStatementsDTOs.size() > 0){
                return bankStatementsDTOs;
            }
            else{
                return null;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in checkBankStatements method. . .");
            LOG.error(e);
            return null;
        }
    }

    @Override
	public boolean checkBankProcessInstance(String filename){
        LOG.log(Level.DEBUG, () -> "start checkBankProcessInstance method. . .");

        try {
            List<BankProcessInstanceDTO> bankProcessInstanceDTOs = new ArrayList<>();
            bankProcessInstanceDTOs = dbOracleService.getBankProcessInstanceDTOs(filename);

            if (bankProcessInstanceDTOs.size() > 0){
                if (bankProcessInstanceDTOs.get(0).getTransformationStatus().equalsIgnoreCase("TRANSFORMED_ERP")){ //Get top result
                    return true;
                }
                else{
                    String body = "<body>Hello, <br><br>";
                    String subject = environment.getProperty("inbound.bank.paids.query.2.failure.subject");

                    if (filename.contains("EAS_CB_MO_")){
                        subject = subject.replace("Bank Paids", "EAS CB MO");
                    }
    
                    body += bankProcessInstanceDTOs.get(0).getBatchName();
                    body += " does not have TRANSFORMED_ERP status.";

                    body += "<br><br>" +
                            "Thanks & Regards, <br>" + 
                            "Bot</body>";

                    q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                                environment.getProperty("mail.argus.cc", String[].class), 
                                subject, body, execTimestamp);

                    return false;
                }
            }
            else{
                String body = "<body>Hello, <br><br>";
                String subject = environment.getProperty("inbound.bank.paids.query.1.failure.subject");

                if (filename.contains("EAS_CB_MO_")){
                    body += "No results found after querying for " + "%" + filename + "%" + " in Process Instance Table in Inbound EAS CB MO Validation process.";
                    subject = subject.replace("Bank Paids", "EAS CB MO");
                }
                else{
                    body += "No results found after querying for " + "%" + filename + "%" + " in Process Instance Table in Inbound Bank Paids Validation process.";
                }
    

                body += "<br><br>" +
                        "Thanks & Regards, <br>" + 
                        "Bot</body>";

                q2cCommonService.sendEmail(environment.getProperty("mail.argus.orca.email", String[].class), 
                            environment.getProperty("mail.argus.cc", String[].class), 
                            subject, body, execTimestamp);

                return false;
            }

        } catch (Exception e){
            LOG.log(Level.DEBUG, () -> "error in checkBankProcessInstance method. . .");
            LOG.error(e);
            return false;
        }
    }

}
