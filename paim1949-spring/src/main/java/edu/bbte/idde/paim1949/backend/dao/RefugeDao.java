package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Region;

import java.util.Collection;

public interface RefugeDao extends Dao<Refuge> {
    Collection<Refuge> findByRegion(Region region);
}
