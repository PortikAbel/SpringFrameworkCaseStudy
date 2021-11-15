package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;

public class RefugeJdbcDao extends AbstractJdbcDao<Refuge> implements RefugeDao {
    public RefugeJdbcDao() {
        super(Refuge.class);
    }
}
