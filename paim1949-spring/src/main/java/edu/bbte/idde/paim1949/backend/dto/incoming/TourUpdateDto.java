package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
public class TourUpdateDto implements Serializable {

    @Positive
    Float distanceInKm;

    @Positive
    Integer elevationInM;

    @Valid
    @NotNull
    ReferencedRegionDto region;

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
