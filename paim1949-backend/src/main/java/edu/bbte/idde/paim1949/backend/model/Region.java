package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.annotation.RefByMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Region extends BaseEntity {
    private String name;
    @IgnoreColumn
    @RefByMany(refTableName = "Tour")
    private Collection<Long> tourIds;
    @IgnoreColumn
    @RefByMany(refTableName = "Refuge")
    private Collection<Long> refugeIds;
}
