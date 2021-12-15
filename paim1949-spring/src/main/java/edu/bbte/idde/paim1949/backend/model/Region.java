package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.annotation.RefByMany;
import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Region extends BaseEntity {
    private String name;
    @IgnoreColumn
    @RefByMany(refTableName = "Tour")
    private Collection<Long> tourIds;
    @IgnoreColumn
    @RefByMany(refTableName = "Refuge")
    private Collection<Long> refugeIds;
}
