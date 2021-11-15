package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TourMemDao extends AbstractMemDao<Tour> implements TourDao {
    static {
        TourDao tourDao = new TourMemDao();
        tourDao.create(new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 1));
        tourDao.create(new Tour(26.4f, 1733, Tour.SignShape.TRIANGLE, Tour.SignColour.YELLOW, 2));
        tourDao.create(new Tour(23.1f, 1587, Tour.SignShape.LINE, Tour.SignColour.RED, 2));
    }
}
