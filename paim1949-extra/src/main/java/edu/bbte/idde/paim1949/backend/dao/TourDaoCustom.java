package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.dto.incoming.requestparam.TourFilterDto;
import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Collection;

public interface TourDaoCustom {

    Collection<Tour> findByFilter(TourFilterDto tourFilterDto);
}
