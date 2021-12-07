package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.DaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("dev")
public class MemDaoFactory implements DaoFactory {
    private TourDao tourDao;
    private RegionDao regionDao;
    private RefugeDao refugeDao;

    @Override
    public TourDao getTourDao() {
        if (tourDao == null) {
            tourDao = new TourMemDao();
        }
        return tourDao;
    }

    @Override
    public RegionDao getRegionDao() {
        if (regionDao == null) {
            regionDao = new RegionMemDao();
        }
        return regionDao;
    }

    @Override
    public RefugeDao getRefugeDao() {
        if (refugeDao == null) {
            refugeDao = new RefugeMemDao();
        }
        return refugeDao;
    }
}
