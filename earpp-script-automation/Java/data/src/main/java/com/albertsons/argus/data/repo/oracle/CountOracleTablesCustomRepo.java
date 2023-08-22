package com.albertsons.argus.data.repo.oracle;

import java.util.List;

import com.albertsons.argus.data.bo.common.AbstractBo;
import com.albertsons.argus.data.bo.oracle.custom.OracleCountCustomBO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountOracleTablesCustomRepo extends CrudRepository<AbstractBo, Long>{
    @Query(nativeQuery = true)
    public List<OracleCountCustomBO> getOracleDBCountCustomBOs();
}