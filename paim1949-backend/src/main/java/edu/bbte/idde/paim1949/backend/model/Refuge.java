package edu.bbte.idde.paim1949.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Refuge extends BaseEntity {
    private Integer nrOfRooms;
    private Integer nrOfBeds;
    private Boolean isOpenAtWinter;
    private Long regionId;
    private Collection<Long> tourIds;
}
