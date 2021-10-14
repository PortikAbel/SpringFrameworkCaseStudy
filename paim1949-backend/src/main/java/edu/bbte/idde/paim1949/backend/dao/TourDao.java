package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Collection;

public interface TourDao {
    Collection<Tour> findAll();

    Tour findById(Long id);

    Tour create(Tour tour);

    Tour update(Long id, Tour tour);

    Tour delete(Long id);
}
