package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.DaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;

public class JdbcDaoFactory implements DaoFactory {
    private TourDao tourDao;
    private RegionDao regionDao;
    private RefugeDao refugeDao;

    @Override
    public TourDao getTourDao() {
        if (tourDao == null) {
            tourDao = new TourJdbcDao();
        }
        return tourDao;
    }

    @Override
    public RegionDao getRegionDao() {
        if (regionDao == null) {
            regionDao = new RegionJdbcDao();
        }
        return regionDao;
    }

    @Override
    public RefugeDao getRefugeDao() {
        if (refugeDao == null) {
            refugeDao = new RefugeJdbcDao();
        }
        return refugeDao;
    }
}
