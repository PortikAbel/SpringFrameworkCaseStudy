package edu.bbte.idde.paim1949.desktop;

import edu.bbte.idde.paim1949.backend.dao.AbstractDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Region;
import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Collection;

public class TourManager {
    private static final RegionDao REGION_SERVER = AbstractDaoFactory.getDaoFactory().getRegionDao();
    private static final TourDao TOUR_SERVER = AbstractDaoFactory.getDaoFactory().getTourDao();
    private static final RefugeDao REFUGE_SERVER = AbstractDaoFactory.getDaoFactory().getRefugeDao();

    private static void populateRegions() {
        Region region1 = new Region();
        region1.setName("Radnai");
        REGION_SERVER.create(region1);

        Region region2 = new Region();
        region2.setName("Hargita");
        REGION_SERVER.create(region2);
    }

    private static void populateTours() {
        Tour tour1 = new Tour();
        tour1.setDistanceInKm(12.3f);
        tour1.setElevationInM(787);
        tour1.setSignShape(Tour.SignShape.CIRCLE.name());
        tour1.setSignColour(Tour.SignColour.RED.name());
        tour1.setDaysRecommended(1);
        TOUR_SERVER.create(tour1);

        Tour tour2 = new Tour();
        tour2.setDistanceInKm(26.4f);
        tour2.setElevationInM(1733);
        tour2.setSignShape(Tour.SignShape.TRIANGLE.name());
        tour2.setSignColour(Tour.SignColour.YELLOW.name());
        tour2.setDaysRecommended(2);
        TOUR_SERVER.create(tour2);

        Tour tour3 = new Tour();
        tour3.setDistanceInKm(23.1f);
        tour3.setElevationInM(1587);
        tour3.setSignShape(Tour.SignShape.LINE.name());
        tour3.setSignColour(Tour.SignColour.RED.name());
        tour3.setDaysRecommended(2);
        TOUR_SERVER.create(tour3);
    }

    private static void populateRefuges() {
        Refuge refuge = new Refuge();
        refuge.setNrOfRooms(1);
        refuge.setNrOfBeds(8);
        refuge.setIsOpenAtWinter(true);
        refuge.setRegion(REGION_SERVER.findById(2L));
        REFUGE_SERVER.create(refuge);
    }

    public static void main(String[] args) {
        if (REGION_SERVER.findAll().isEmpty()) {
            populateRegions();
        }
        if (TOUR_SERVER.findAll().isEmpty()) {
            populateTours();
        }
        if (REFUGE_SERVER.findAll().isEmpty()) {
            populateRefuges();
        }

        Collection<Region> regions = REGION_SERVER.findAll();
        for (Region region: regions) {
            REGION_SERVER.findById(region.getId());
        }

        Long firstTourId = TOUR_SERVER.findAll().iterator().next().getId();

        if (TOUR_SERVER.findAll().size() > 1) {
            TOUR_SERVER.findById(firstTourId + 1);
            TOUR_SERVER.delete(firstTourId + 1);
            TOUR_SERVER.findAll();
        }

        Tour oldTour = TOUR_SERVER.findById(firstTourId);
        Tour newTour = new Tour();
        newTour.setDistanceInKm(oldTour.getDistanceInKm());
        newTour.setElevationInM(oldTour.getElevationInM() + 1);
        newTour.setSignShape(oldTour.getSignShape());
        newTour.setSignColour(oldTour.getSignColour());
        newTour.setDaysRecommended(oldTour.getDaysRecommended());
        TOUR_SERVER.update(firstTourId, newTour);
        TOUR_SERVER.findById(firstTourId);
    }
}
