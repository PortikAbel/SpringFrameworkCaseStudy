package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
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
    private Collection<Long> tourIds;
    @IgnoreColumn
    private Collection<Long> refugeIds;
}
