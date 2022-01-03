package edu.bbte.idde.paim1949.backend.dto.outgoing;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TourReducedDto implements Serializable {
    Long id;
    Float distanceInKm;
    Integer elevationInM;
}
