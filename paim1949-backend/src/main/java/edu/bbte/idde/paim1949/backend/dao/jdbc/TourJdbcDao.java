package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TourJdbcDao extends AbstractJdbcDao<Tour> implements TourDao {

    private static class InstanceHolder {
        public static final TourJdbcDao INSTANCE = new TourJdbcDao();
    }

    public static TourJdbcDao getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private TourJdbcDao() {
        super(Tour.class);
    }
}
