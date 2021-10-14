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
    private static final AtomicLong NEXTID = new AtomicLong();
    private static final Logger LOG = LoggerFactory.getLogger(TourMemDao.class);

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
        Long nextId = NEXTID.getAndIncrement();
        tour.setId(nextId);
        DATABASE.put(nextId, tour);

        LOG.info("Tour created: " + tour);
        return tour;
    }

    @Override
    public Tour update(Long id, Tour tour) {
        Tour oldTour = DATABASE.get(id);
        DATABASE.replace(id, tour);

        LOG.info("Tour being updated.\n"
                + "old value: " + oldTour
                + "new value: " + tour
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
