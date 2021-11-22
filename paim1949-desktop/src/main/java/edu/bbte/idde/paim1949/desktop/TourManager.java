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
    private final static RegionDao regionServer = AbstractDaoFactory.getDaoFactory().getRegionDao();
    private final static TourDao tourServer = AbstractDaoFactory.getDaoFactory().getTourDao();
    private final static RefugeDao refugeServer = AbstractDaoFactory.getDaoFactory().getRefugeDao();

    private static void populateRegions()
    {
        Region region1 = new Region();
        region1.setName("Radnai");
        regionServer.create(region1);

        Region region2 = new Region();
        region2.setName("Hargita");
        regionServer.create(region2);
    }

    private static void populateTours()
    {
        Tour tour1 = new Tour();
        tour1.setDistanceInKm(12.3f);
        tour1.setElevationInM(787);
        tour1.setSignShape(Tour.SignShape.CIRCLE.name());
        tour1.setSignColour(Tour.SignColour.RED.name());
        tour1.setDaysRecommended(1);
        tour1.setRegionId(4L);
        tourServer.create(tour1);

        Tour tour2 = new Tour();
        tour2.setDistanceInKm(26.4f);
        tour2.setElevationInM(1733);
        tour2.setSignShape(Tour.SignShape.TRIANGLE.name());
        tour2.setSignColour(Tour.SignColour.YELLOW.name());
        tour2.setDaysRecommended(2);
        tour2.setRegionId(4L);
        tourServer.create(tour2);

        Tour tour3 = new Tour();
        tour3.setDistanceInKm(23.1f);
        tour3.setElevationInM(1587);
        tour3.setSignShape(Tour.SignShape.LINE.name());
        tour3.setSignColour(Tour.SignColour.RED.name());
        tour3.setDaysRecommended(2);
        tour3.setRegionId(3L);
        tourServer.create(tour3);
    }

    private static void populateRefuges()
    {
        Refuge refuge = new Refuge();
        refuge.setNrOfRooms(1);
        refuge.setNrOfBeds(8);
        refuge.setIsOpenAtWinter(true);
        refuge.setRegionId(4L);
        refugeServer.create(refuge);
    }

    public static void main(String[] args) {
        if (regionServer.findAll().isEmpty()) {
            populateRegions();
        }
        if (tourServer.findAll().isEmpty()) {
            populateTours();
        }
        if (refugeServer.findAll().isEmpty()) {
            populateRefuges();
        }

        Collection<Region> regions = regionServer.findAll();
        for (Region region: regions) {
            regionServer.findById(region.getId());
        }

        tourServer.findAll();

        tourServer.findById(1L);

        tourServer.delete(1L);
        tourServer.findAll();

        Tour tour = tourServer.findById(0L);
        tour.setElevationInM(tour.getElevationInM() + 1);
        tourServer.update(0L, tour);
        tourServer.findById(0L);
    }
}
