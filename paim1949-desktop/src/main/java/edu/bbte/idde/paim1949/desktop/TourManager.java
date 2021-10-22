package edu.bbte.idde.paim1949.desktop;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.dao.mem.TourMemDao;
import edu.bbte.idde.paim1949.backend.model.Tour;

public class TourManager {
    public static void main(String[] args) {
        TourDao tourServer = new TourMemDao();

        tourServer.create(new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 1));
        tourServer.create(new Tour(26.4f, 1733, Tour.SignShape.TRIANGLE, Tour.SignColour.YELLOW, 2));
        tourServer.create(new Tour(23.1f, 1587, Tour.SignShape.LINE, Tour.SignColour.RED, 2));

        tourServer.findAll();

        tourServer.findById(1L);

        tourServer.delete(1L);
        tourServer.findAll();

        tourServer.update(0L, new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 2));
        tourServer.findById(0L);
    }
}
