package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.Refuge;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("RegionRepository")
public interface RefugeRepository extends ReactiveSortingRepository<Refuge, Long> {
}
