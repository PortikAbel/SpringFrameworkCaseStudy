package edu.bbte.idde.paim1949.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TourServer {
    private final List<Tour> tours;
    private static final Logger LOG = LoggerFactory.getLogger(TourServer.class);

    public TourServer() {
        tours = new ArrayList<>();
    }

    public void addTour(Tour tour) {
        tours.add(tour);
        LOG.info("Tour added: " + tour);
    }

    public List<Tour> getTours() {
        LOG.info("Tours requested: \n" + tours);
        return tours;
    }

    public Tour getTour(int index) {
        Tour tour = tours.get(index);
        LOG.info("Tour requested: " + tour);
        return tour;
    }

    public void updateTour(int index, Tour tour) {
        LOG.info("Tour being updated.\n"
                + "old value: " + tours.get(index)
                + "new value: " + tour
        );
        tours.set(index, tour);
    }

    public void deleteTour(int index) {
        Tour tour = tours.remove(index);
        LOG.info("Tour removed: " + tour);
    }
}
