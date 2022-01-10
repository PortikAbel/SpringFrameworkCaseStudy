package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Region;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class RefugeMemDao extends AbstractMemDao<Refuge> implements RefugeDao {
    @Override
    public Collection<Refuge> findByRegion(Region region) {
        log.info("Refuges with region id {} requested", region.getId());
        return dataBase.values().stream()
                .filter(refuge -> refuge.getRegion().equals(region))
                .collect(Collectors.toList());
    }
}
