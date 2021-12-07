package edu.bbte.idde.paim1949.backend.dto.outgoing;

import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TourDetailsDto extends TourReducedDto {
    Tour.SignShape signShape;
    Tour.SignColour signColour;
    Integer daysRecommended;
}
