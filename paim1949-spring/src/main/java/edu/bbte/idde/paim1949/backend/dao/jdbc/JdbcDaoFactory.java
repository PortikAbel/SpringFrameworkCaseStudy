package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.DaoFactory;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

@Configuration
@Profile("prod")
public class JdbcDaoFactory implements DaoFactory {
    @Override
    @Bean
    @Scope("prototype")
    public TourDao getTourDao() {
        return new TourJdbcDao();
    }

    @Override
    @Bean
    @Scope("prototype")
    public RegionDao getRegionDao() {
        return new RegionJdbcDao();
    }

    @Override
    @Bean
    @Scope("prototype")
    public RefugeDao getRefugeDao() {
        return new RefugeJdbcDao();
    }
}
