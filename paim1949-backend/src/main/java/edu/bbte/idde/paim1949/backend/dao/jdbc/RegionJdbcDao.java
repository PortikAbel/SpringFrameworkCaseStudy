package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.Region;

public class RegionJdbcDao extends AbstractJdbcDao<Region> implements RegionDao {
    public RegionJdbcDao() {
        super(Region.class);
    }
}
