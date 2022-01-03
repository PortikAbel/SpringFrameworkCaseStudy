package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Refuge extends BaseEntity {
    private Integer nrOfRooms;
    private Integer nrOfBeds;
    private Boolean isOpenAtWinter;
    @RefToOne(refTableName = "Region")
    private Region region;
}
