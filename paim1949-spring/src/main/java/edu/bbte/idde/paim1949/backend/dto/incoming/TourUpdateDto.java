package edu.bbte.idde.paim1949.backend.dto.incoming;

import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.Data;

import java.io.Serializable;

@Data
public class TourUpdateDto implements Serializable {
    Float distanceInKm;
    Integer elevationInM;
    Long regionId;
    Tour.SignShape signShape;
    Tour.SignColour signColour;
    Integer daysRecommended;
}
