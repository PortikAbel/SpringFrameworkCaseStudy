package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Region;
import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Collection;

public interface TourDao extends Dao<Tour> {
    Collection<Tour> findByRegion(Region region);
}
