package edu.bbte.idde.paim1949.backend.dto.incoming.requestparam;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.util.List;

@Data
public class TourFilterDto implements Serializable {

    List<String> signShapes;

    List<String> signColors;

    @NumberFormat
    String minElevation;

    @NumberFormat
    String maxElevation;

    @NumberFormat
    String minDistance;

    @NumberFormat
    String maxDistance;

    @NumberFormat
    String minDaysRecommended;

    @NumberFormat
    String maxDaysRecommended;
}
