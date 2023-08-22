package com.albertsons.argus.q2c.inbound.invoices.service;

import java.util.List;

import com.albertsons.argus.domaindbq2c.dto.InterfaceLinesDTO;
import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessFileDetailsDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.q2c.exception.Q2cInsuranceArException;

/**
 * @author kbuen03
 * @since 01/25/23
 * @version 1.0
 * 
 */
public interface ArInvoicesService {
    final String PROCESS_INSTANCE_TRANSFROMATION_STS_VALUE= "TRANSFORMED_ERP";
    final String PROCESS_INSTACE_STS_VALUE = "SUCCESS";
    final String PROCSS_FILE_DTLS_LOAD_STS = "COMPLETED";
    final String INTERFACE_LNE_STS = "VALIDATED";
    final String JOB_HSTY_LNE_JOB_STS = "SUCCESS";
    final String AUTOINVOICEIMPORTS_CONST = "AutoInvoiceImportEss";



    public List<ProcessInstanceDTO> getProcessInstance(String fileName, String dateStr) throws Q2cInsuranceArException;

    /**
     * @apiNote - This service uses transformation_status is TRANSFORMED_ERP and status is SUCCESS
     * @param processInstanceDTO
     * @return
     */
    public boolean isProcessInstanceTrSuccess(ProcessInstanceDTO processInstanceDTO);

    public List<ProcessFileDetailsDTO> getProcessFileDetail(String fileName)throws Q2cInsuranceArException;
    
    /**
     * @apiNote - This service uses status is COMPLETED
     * @param processFileDetailsDTO
     * @return
     */
    public boolean isProcessFileDetailsSuccess(ProcessFileDetailsDTO processFileDetailsDTO);

    public List<InterfaceLinesDTO> getInterfaceLine(List<String> fileNames) throws Q2cInsuranceArException;

    /**
     * @apiNote - This service uses status is VALIDATED
     * @param interfaceLinesDTO
     * @return
     */
    public boolean isInterfaceLineSuccess(InterfaceLinesDTO interfaceLinesDTO);

    public List<JobHistoryDTO> getJobHistoryInLines(List<String> filename) throws Q2cInsuranceArException;

       /**
     * @apiNote - This service uses status is SUCCESS
     * @param jobHistoryDTO
     * @return
     */
    public boolean isJobHistoryInLineSuccess(JobHistoryDTO jobHistoryDTO);

    public void processArInvoices(String fileName, String dateStr) throws Q2cInsuranceArException;
}
