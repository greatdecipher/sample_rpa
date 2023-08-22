package com.albertsons.argus.q2c.service;

import java.util.List;

import com.albertsons.argus.domaindbq2c.dto.JobHistoryDTO;
import com.albertsons.argus.domaindbq2c.dto.ProcessInstanceDTO;
import com.albertsons.argus.q2c.exception.Q2CEmployeePayrollException;

public interface EmployeePayrollService {
    final String EMP_PAYROLL_PREFIX = "EP_ISSUE_";
    final String PROCESS_INSTANCE_TRANSFROMATION_STS_VALUE= "TRANSFORMED_ERP";
    final String JOB_STS_VALUE= "SUBMITTED";
    final String JOB_NAME_VALUE= "ImportPayrollChecks";

    public void employeePayroll(String fileName, String execTime) throws Q2CEmployeePayrollException;

       /**
     * @apiNote - This service uses transformation_status is TRANSFORMED_ERP and status is NULL
     * @param processInstanceDTO
     * @return
     */
    public boolean isProcessInstanceTrSuccess(ProcessInstanceDTO processInstanceDTO);

    public List<JobHistoryDTO> getJobHistoryTxnDto(String fileName) throws Q2CEmployeePayrollException;

    public boolean isJobHistoryTrxtn(JobHistoryDTO jobHistoryDTO);

}
