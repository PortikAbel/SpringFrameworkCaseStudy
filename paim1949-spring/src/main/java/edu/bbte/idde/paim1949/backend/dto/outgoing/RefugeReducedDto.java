package edu.bbte.idde.paim1949.backend.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RefugeReducedDto implements Serializable {
    Long id;
    private Integer nrOfRooms;
    private Integer nrOfBeds;
    private Boolean isOpenAtWinter;
}