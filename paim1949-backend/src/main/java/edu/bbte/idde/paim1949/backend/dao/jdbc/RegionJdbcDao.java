package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.Region;

public final class RegionJdbcDao extends AbstractJdbcDao<Region> implements RegionDao {

    private static class InstanceHolder {
        public static final RegionJdbcDao INSTANCE = new RegionJdbcDao();
    }

    public static RegionJdbcDao getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private RegionJdbcDao() {
        super(Region.class);
    }
}
