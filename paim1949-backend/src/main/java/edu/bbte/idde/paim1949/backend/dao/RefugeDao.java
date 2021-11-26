package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Refuge;

import java.util.Collection;

public interface RefugeDao extends Dao<Refuge> {
    Collection<Refuge> findByRegionId(Long regionId);
}
