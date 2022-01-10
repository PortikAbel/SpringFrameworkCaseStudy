package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.DaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;

public class JdbcDaoFactory implements DaoFactory {
    private final TourDao tourDao = TourJdbcDao.getInstance();
    private final RegionDao regionDao = RegionJdbcDao.getInstance();
    private final RefugeDao refugeDao = RefugeJdbcDao.getInstance();

    @Override
    public TourDao getTourDao() {
        return tourDao;
    }

    @Override
    public RegionDao getRegionDao() {
        return regionDao;
    }

    @Override
    public RefugeDao getRefugeDao() {
        return refugeDao;
    }
}
