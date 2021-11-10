package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.dao.mem.TourMemDao;

public class TourDaoFactory {
    private static TourDao tourDao;

    public  static  synchronized TourDao getTourMemDao() {
        if (tourDao == null) {
            tourDao = new TourMemDao();
        }
        return tourDao;
    }
}
