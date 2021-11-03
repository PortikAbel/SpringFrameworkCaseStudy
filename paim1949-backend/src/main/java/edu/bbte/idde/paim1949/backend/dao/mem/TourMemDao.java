package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TourMemDao implements TourDao {
    private static final Map<Long, Tour> DATABASE = new ConcurrentHashMap<>();
    private static final AtomicLong NEXT_ID = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(TourMemDao.class);

    static {
        TourDao tourDao = new TourMemDao();
        tourDao.create(new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 1));
        tourDao.create(new Tour(26.4f, 1733, Tour.SignShape.TRIANGLE, Tour.SignColour.YELLOW, 2));
        tourDao.create(new Tour(23.1f, 1587, Tour.SignShape.LINE, Tour.SignColour.RED, 2));
    }

    @Override
    public Collection<Tour> findAll() {
        LOG.info("All tours requested");
        return DATABASE.values();
    }

    @Override
    public Tour findById(Long id) {
        Tour tour = DATABASE.get(id);
        LOG.info("Tour requested: " + tour);
        return tour;
    }

    @Override
    public Tour create(Tour tour) {
        Long nextId = NEXT_ID.getAndIncrement();
        tour.setId(nextId);
        DATABASE.put(nextId, tour);

        LOG.info("Tour created: " + tour);
        return tour;
    }

    @Override
    public Tour update(Long id, Tour tour) {
        Tour oldTour = DATABASE.get(id);
        DATABASE.replace(id, tour);

        LOG.info("Tour being updated."
                + "\n\told value: " + oldTour
                + "\n\tnew value: " + tour
        );
        return oldTour;
    }

    @Override
    public Tour delete(Long id) {
        Tour tour = DATABASE.remove(id);

        LOG.info("Tour removed: " + tour);
        return tour;
    }
}
