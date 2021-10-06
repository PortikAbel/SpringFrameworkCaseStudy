package edu.bbte.idde.paim1949.frontend;

import edu.bbte.idde.paim1949.backend.Tour;
import edu.bbte.idde.paim1949.backend.TourServer;

import org.slf4j.LoggerFactory;
import java.util.logging.Logger;

public class TourManager {
    public static final Logger LOG = Logger.getLogger(LoggerFactory.class.getName());

    public static void main(String[] args) {
        TourServer tourServer = new TourServer();

        tourServer.addTour(new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 1));
        tourServer.addTour(new Tour(26.4f, 1733, Tour.SignShape.TRIANGLE, Tour.SignColour.YELLOW, 2));
        tourServer.addTour(new Tour(23.1f, 1587, Tour.SignShape.LINE, Tour.SignColour.RED, 2));

        LOG.info(tourServer.getTours().toString());
        LOG.info(tourServer.getTour(1).toString());
        tourServer.deleteTour(1);
        LOG.info(tourServer.getTours().toString());
    }
}
