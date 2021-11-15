package edu.bbte.idde.paim1949.backend.dao;

public interface DaoFactory {
    TourDao getTourDao();
    RegionDao getRegionDao();
    RefugeDao getRefugeDao();
}
