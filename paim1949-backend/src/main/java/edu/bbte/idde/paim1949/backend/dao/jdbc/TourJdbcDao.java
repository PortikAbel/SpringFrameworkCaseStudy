package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;

public class TourJdbcDao extends AbstractJdbcDao<Tour> implements TourDao {
    public TourJdbcDao() {
        super(Tour.class);
    }
}
