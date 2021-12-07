package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.DaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Region;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;

public class RegionMemDao extends AbstractMemDao<Region> implements RegionDao {
    @Autowired
    private DaoFactory daoFactory;

    @Override
    public Collection<Region> findAll() {
        Collection<Region> regions = super.findAll();
        for (Region region: regions) {
            region.setRefugeIds(daoFactory.getRefugeDao()
                    .findByRegionId(region.getId())
                    .stream().map(BaseEntity::getId)
                    .collect(Collectors.toList()));
            region.setTourIds(daoFactory.getTourDao()
                    .findByRegionId(region.getId())
                    .stream().map(BaseEntity::getId)
                    .collect(Collectors.toList()));
        }
        return regions;
    }

    @Override
    public Region findById(Long id) {
        Region region = super.findById(id);
        region.setRefugeIds(daoFactory.getRefugeDao()
                .findByRegionId(region.getId())
                .stream().map(BaseEntity::getId)
                .collect(Collectors.toList()));
        region.setTourIds(daoFactory.getTourDao()
                .findByRegionId(region.getId())
                .stream().map(BaseEntity::getId)
                .collect(Collectors.toList()));
        return region;
    }
}
