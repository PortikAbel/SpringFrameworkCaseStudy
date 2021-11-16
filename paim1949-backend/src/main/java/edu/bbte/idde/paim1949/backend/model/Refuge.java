package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Refuge extends BaseEntity {
    private Integer nrOfRooms;
    private Integer nrOfBeds;
    private Boolean isOpenAtWinter;
    @RefToOne(refTableName = "Region")
    private Long regionId;
}
