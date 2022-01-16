package edu.bbte.idde.paim1949.backend.dao.jpa;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface TourSpringDataDao extends JpaRepository<Tour, Long>, TourDao {
}
