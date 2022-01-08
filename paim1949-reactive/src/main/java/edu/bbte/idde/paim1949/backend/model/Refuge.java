package edu.bbte.idde.paim1949.backend.model;

import lombok.*;
import org.springframework.stereotype.Repository;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Refuge extends BaseEntity {

    private Integer nrOfRooms;

    private Integer nrOfBeds;

    private Boolean isOpenAtWinter;

    private Region region;
}
