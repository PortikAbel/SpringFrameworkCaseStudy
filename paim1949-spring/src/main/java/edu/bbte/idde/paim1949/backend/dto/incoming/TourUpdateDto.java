package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class TourUpdateDto implements Serializable {

    @Positive
    Float distanceInKm;

    @Positive
    Integer elevationInM;

    @PositiveOrZero
    Long regionId;

    @Pattern(
            regexp = "(CIRCLE)|(TRIANGLE)|(LINE)|(CROSS)",
            message = "Non-existent sign shape"
    )
    String signShape;

    @Pattern(
            regexp = "(RED)|(BLUE)|(YELLOW)",
            message = "Non-existent sign colour"
    )
    String signColour;

    @Positive
    Integer daysRecommended;

}
