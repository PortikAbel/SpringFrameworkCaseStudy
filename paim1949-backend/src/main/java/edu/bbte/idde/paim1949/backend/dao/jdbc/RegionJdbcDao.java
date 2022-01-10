package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.Region;

public class RegionJdbcDao extends AbstractJdbcDao<Region> implements RegionDao {

    private static RegionJdbcDao instance;

    public static RegionJdbcDao getInstance() {
        if (instance == null) {
            instance = new RegionJdbcDao();
        }
        return instance;
    }

    private RegionJdbcDao() {
        super(Region.class);
    }
}
