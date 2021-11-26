package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.config.ConfigFactory;
import edu.bbte.idde.paim1949.backend.dao.jdbc.JdbcDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.mem.MemDaoFactory;

public abstract class AbstractDaoFactory {
    private static DaoFactory daoFactory;

    public static synchronized DaoFactory getDaoFactory() {
        if (daoFactory == null) {
            String daoType = ConfigFactory.getConfig().getDaoType();
            if ("mem".equalsIgnoreCase(daoType)) {
                daoFactory = new MemDaoFactory();
            } else if ("jdbc".equalsIgnoreCase(daoType)) {
                daoFactory = new JdbcDaoFactory();
            }
        }
        return daoFactory;
    }
}
