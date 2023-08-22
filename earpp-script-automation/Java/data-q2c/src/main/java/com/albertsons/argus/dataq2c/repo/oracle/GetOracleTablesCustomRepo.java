package com.albertsons.argus.dataq2c.repo.oracle;

import java.util.List;

import com.albertsons.argus.dataq2c.bo.common.AbstractBo;
import com.albertsons.argus.dataq2c.bo.oracle.custom.BankProcessInstanceBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.BankStatementsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.InterfaceLinesBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.JobHistoryBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.LockboxHdrTrailPayBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.OutboundProcessDetailsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessFileDetailsBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessFileDetailsFilteredBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.ProcessInstanceBO;
import com.albertsons.argus.dataq2c.bo.oracle.custom.TargetedProcessFileDetailsBO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GetOracleTablesCustomRepo extends CrudRepository<AbstractBo, Long>{
    @Query(nativeQuery = true)
    public List<ProcessInstanceBO> getProcessInstanceBOs(@Param("filename") String filename);
    
    @Query(nativeQuery = true)
    public List<ProcessFileDetailsBO> getProcessFileDetailsBOs(@Param("filename") String filenames);

    @Query(nativeQuery = true)
    public List<InterfaceLinesBO> getInterfaceLinesBOs(@Param("filenames") List<String> filenames);

    @Query(nativeQuery = true)
    public List<InterfaceLinesBO> getInterfaceLineCountBos(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<JobHistoryBO> getJobHistoryInLinesBOs(@Param("filenames") List<String> filenames);

    @Query(nativeQuery = true)
    public List<BankStatementsBO> getBankStatementsBOs(@Param("filename") String filename);
    
    @Query(nativeQuery = true)
    public List<BankProcessInstanceBO> getBankProcessInstanceBOs(@Param("filename") String filename);
    
    @Query(nativeQuery = true)
    public List<ProcessInstanceBO> getProcessInstanceByDescBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<JobHistoryBO> getJobHistoryTrxsBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<ProcessFileDetailsFilteredBO> getProcessFileDetailsFilteredBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<ProcessFileDetailsBO> getProcessFileDetailsExactBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<LockboxHdrTrailPayBO> getLockboxHdrTrailPayBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<JobHistoryBO> getJobHistoryLockboxBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<TargetedProcessFileDetailsBO> getTargetedProcessFileDetailsBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getArCustProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getCrosswalkProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getCustBalProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getCustStatementsProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getPosPayAndVoidsProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getPrgExtractsProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getSDChecksProcessDetailsBOs();

    @Query(nativeQuery = true)
    public List<OutboundProcessDetailsBO> getUwareProcessDetailsBOs();
}