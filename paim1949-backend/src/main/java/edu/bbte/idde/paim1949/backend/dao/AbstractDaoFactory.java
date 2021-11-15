package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.config.ConfigFactory;
import edu.bbte.idde.paim1949.backend.dao.jdbc.JdbcDaoFactory;
import edu.bbte.idde.paim1949.backend.dao.mem.MemDaoFactory;

public abstract class AbstractDaoFactory {
    private static DaoFactory daoFactory;

    public static DaoFactory getDaoFactory(){
        if (daoFactory == null) {
            String daoType = ConfigFactory.getConfig().getDaoType();
            if(daoType.equalsIgnoreCase("mem")) {
                daoFactory = new MemDaoFactory();
            } else if (daoType.equalsIgnoreCase("jdbc")) {
                daoFactory = new JdbcDaoFactory();
            }
        }
        return daoFactory;
    }
}
