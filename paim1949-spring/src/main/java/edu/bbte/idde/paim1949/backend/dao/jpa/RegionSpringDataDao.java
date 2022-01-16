package edu.bbte.idde.paim1949.backend.dao.jpa;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.model.Region;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface RegionSpringDataDao extends JpaRepository<Region, Long>, RegionDao {
}
