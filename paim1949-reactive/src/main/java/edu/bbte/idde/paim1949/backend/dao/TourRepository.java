package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Tour;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("RegionRepository")
public interface TourRepository extends ReactiveSortingRepository<Tour, Long> {
}
