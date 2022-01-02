package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Profile("mem")
public class RegionMemDao extends AbstractMemDao<Region> implements RegionDao {
    @Autowired
    private RefugeDao refugeDao;
    @Autowired
    private TourDao tourDao;

    @Override
    public Collection<Region> findAll() {
        Collection<Region> regions = super.findAll();
        for (Region region: regions) {
            region.setRefuges(refugeDao.findByRegionId(region.getId()));
            region.setTours(tourDao.findByRegionId(region.getId()));
        }
        return regions;
    }

    @Override
    public Region findById(Long id) {
        Region region = super.findById(id);
        region.setRefuges(refugeDao.findByRegionId(region.getId()));
        region.setTours(tourDao.findByRegionId(region.getId()));
        return region;
    }
}
