package edu.bbte.idde.paim1949.backend.model;

import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE}
    )
    private Region region;
}
