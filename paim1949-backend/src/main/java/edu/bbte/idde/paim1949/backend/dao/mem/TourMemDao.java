package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public class TourMemDao extends AbstractMemDao<Tour> implements TourDao {
    public TourMemDao() {
        super();
        Tour tour1 = new Tour();
        tour1.setDistanceInKm(12.3f);
        tour1.setElevationInM(787);
        tour1.setSignShape(Tour.SignShape.CIRCLE.name());
        tour1.setSignColour(Tour.SignColour.RED.name());
        tour1.setDaysRecommended(1);
        tour1.setRegionId(3L);
        this.create(tour1);

        Tour tour2 = new Tour();
        tour2.setDistanceInKm(26.4f);
        tour2.setElevationInM(1733);
        tour2.setSignShape(Tour.SignShape.TRIANGLE.name());
        tour2.setSignColour(Tour.SignColour.YELLOW.name());
        tour2.setDaysRecommended(2);
        tour2.setRegionId(3L);
        this.create(tour2);

        Tour tour3 = new Tour();
        tour3.setDistanceInKm(23.1f);
        tour3.setElevationInM(1587);
        tour3.setSignShape(Tour.SignShape.LINE.name());
        tour3.setSignColour(Tour.SignColour.RED.name());
        tour3.setDaysRecommended(2);
        tour3.setRegionId(4L);
        this.create(tour3);
    }

    @Override
    public Collection<Tour> findByRegionId(Long regionId) {
        log.info("Tours with region id {} requested", regionId);
        return dataBase.values().stream()
                .filter(tour -> tour.getRegionId().equals(regionId))
                .collect(Collectors.toList());
    }
}
