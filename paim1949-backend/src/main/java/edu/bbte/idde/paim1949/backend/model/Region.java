package edu.bbte.idde.paim1949.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Region extends BaseEntity {
    private String name;
}
