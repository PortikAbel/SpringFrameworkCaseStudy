package edu.bbte.idde.paim1949.backend.dto.incoming.requestparam;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.List;

@Data
public class TourFilterDto implements Serializable {

    List<@Pattern(
            regexp = "(CIRCLE)|(TRIANGLE)|(LINE)|(CROSS)",
            message = "Non-existent sign shape"
    ) String> signShapes;

    List<@Pattern(
            regexp = "(RED)|(BLUE)|(YELLOW)",
            message = "Non-existent sign colour"
    ) String> signColors;

    @Positive
    Integer minElevation;

    @Positive
    Integer maxElevation;

    @Positive
    Float minDistance;

    @Positive
    Float maxDistance;

    @Positive
    Integer minDaysRecommended;

    @Positive
    Integer maxDaysRecommended;
}
