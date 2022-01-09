package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Region;
import edu.bbte.idde.paim1949.backend.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDao extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    Page<Tour> findByRegion(Region region, Pageable pageable);
}
