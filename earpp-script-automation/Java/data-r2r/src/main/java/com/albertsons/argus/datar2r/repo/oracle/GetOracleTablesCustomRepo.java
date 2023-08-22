package com.albertsons.argus.datar2r.repo.oracle;

import java.util.List;

import com.albertsons.argus.datar2r.bo.common.AbstractBo;
import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessFileDetailsBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.ProcessInstanceBO;
import com.albertsons.argus.datar2r.bo.oracle.custom.RecordBO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GetOracleTablesCustomRepo extends CrudRepository<AbstractBo, Long>{
    @Query(nativeQuery = true)
    public List<ProcessFileDetailsBO> getProcessFileDetailsBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<RecordBO> getRecordBOs(@Param("filename") String filename);

    @Query(nativeQuery = true)
    public List<ProcessInstanceBO> getProcessInstanceBOs(@Param("filename") String filename);
}