package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TourJdbcDao extends AbstractJdbcDao<Tour> implements TourDao {

    private static TourJdbcDao instance;

    public static TourJdbcDao getInstance() {
        if (instance == null) {
            instance = new TourJdbcDao();
        }
        return instance;
    }

    private TourJdbcDao() {
        super(Tour.class);
    }
}
