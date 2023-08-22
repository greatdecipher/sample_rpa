package com.albertsons.argus.data.repo.db2;

import java.util.List;

import com.albertsons.argus.data.bo.common.AbstractBo;
import com.albertsons.argus.data.bo.db2.custom.DB2CountCustomBO;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountDB2TablesCustomRepo extends CrudRepository<AbstractBo, Long>{
    @Query(nativeQuery = true)
    public List<DB2CountCustomBO> getDB2DBCountCustomBOs();
}