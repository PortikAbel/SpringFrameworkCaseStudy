package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class TourMemDao extends AbstractMemDao<Tour> implements TourDao {
    @Override
    public Collection<Tour> findByRegionId(Long regionId) {
        log.info("Tours with region id {} requested", regionId);
        return dataBase.values().stream()
                .filter(tour -> tour.getRegionId().equals(regionId))
                .collect(Collectors.toList());
    }
}
