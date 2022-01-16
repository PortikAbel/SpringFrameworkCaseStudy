package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Region;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Profile("mem")
public class TourMemDao extends AbstractMemDao<Tour> implements TourDao {
    @Override
    public Collection<Tour> findByRegion(Region region) {
        log.info("Tours with region id {} requested", region);
        return dataBase.values().stream()
                .filter(tour -> tour.getRegion().equals(region))
                .collect(Collectors.toList());
    }
}
