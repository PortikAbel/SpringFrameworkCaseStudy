package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class TourCreationDto implements Serializable {

    @Positive
    @NotNull
    Float distanceInKm;

    @Positive
    @NotNull
    Integer elevationInM;

    @PositiveOrZero
    Long regionId;

    @Pattern(
            regexp = "(CIRCLE)|(TRIANGLE)|(LINE)|(CROSS)",
            message = "Non-existent sign shape")
    @NotNull
    String signShape;

    @Pattern(
            regexp = "(RED)|(BLUE)|(YELLOW)",
            message = "Non-existent sign colour")
    @NotNull
    String signColour;

    @Positive
    Integer daysRecommended;

}