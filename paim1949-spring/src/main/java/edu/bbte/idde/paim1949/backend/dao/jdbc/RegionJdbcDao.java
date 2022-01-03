package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class RegionJdbcDao extends AbstractJdbcDao<Region> implements RegionDao {
    @Autowired
    private RefugeDao refugeDao;
    @Autowired
    private TourDao tourDao;

    public RegionJdbcDao() {
        super(Region.class);
    }

    @Override
    public Collection<Region> findAll() {
        Collection<Region> regions = super.findAll();
        for (Region region: regions) {
            region.setRefuges(refugeDao.findByRegion(region));
            region.setTours(tourDao.findByRegion(region));
        }
        return regions;
    }

    @Override
    public Optional<Region> findById(Long id) {
        Optional<Region> optionalRegion = super.findById(id);
        if (!optionalRegion.isPresent()) {
            return Optional.empty();
        }
        Region region = optionalRegion.get();
        region.setRefuges(refugeDao.findByRegion(region));
        region.setTours(tourDao.findByRegion(region));
        return Optional.of(region);
    }
}
