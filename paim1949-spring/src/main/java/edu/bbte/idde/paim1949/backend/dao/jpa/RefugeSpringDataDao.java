package edu.bbte.idde.paim1949.backend.dao.jpa;

import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface RefugeSpringDataDao extends JpaRepository<Refuge, Long>, RefugeDao {
}
