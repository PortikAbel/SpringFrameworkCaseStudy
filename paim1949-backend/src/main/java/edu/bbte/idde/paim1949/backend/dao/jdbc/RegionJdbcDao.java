package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Region;

import java.util.Collection;
import java.util.stream.Collectors;

public class RegionJdbcDao extends AbstractJdbcDao<Region> implements RegionDao {
    public RegionJdbcDao() {
        super(Region.class);
    }

    @Override
    public Collection<Region> findAll() {
        Collection<Region> regions = super.findAll();
        for (Region region: regions) {
            region.setRefugeIds(AbstractDaoFactory.getDaoFactory()
                    .getRefugeDao().findByRegionId(region.getId())
                    .stream().map(BaseEntity::getId)
                    .collect(Collectors.toList()));
            region.setTourIds(AbstractDaoFactory.getDaoFactory()
                    .getTourDao().findByRegionId(region.getId())
                    .stream().map(BaseEntity::getId)
                    .collect(Collectors.toList()));
        }
        return regions;
    }

    @Override
    public Region findById(Long id) {
        Region region = super.findById(id);
        region.setRefugeIds(AbstractDaoFactory.getDaoFactory()
                .getRefugeDao().findByRegionId(region.getId())
                .stream().map(BaseEntity::getId)
                .collect(Collectors.toList()));
        region.setTourIds(AbstractDaoFactory.getDaoFactory()
                .getTourDao().findByRegionId(region.getId())
                .stream().map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return region;
    }
}
