package edu.bbte.idde.paim1949.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Tour extends BaseEntity {
    public enum SignShape { CIRCLE, TRIANGLE, LINE, CROSS }

    public enum SignColour { RED, BLUE, YELLOW }

    private Float distanceInKm;
    private Integer elevationInM;
    private SignShape signShape;
    private SignColour signColour;
    private Integer daysRecommended;
    private Long regionId;
    private Collection<Long> refugeIds;

    public Tour() {
        super();
    }

    public Tour(Float distanceInKm, Integer elevationInM,
                SignShape signShape, SignColour signColour,
                Integer daysRecommended) {
        super();

        this.distanceInKm = distanceInKm;
        this.elevationInM = elevationInM;
        this.signShape = signShape;
        this.signColour = signColour;
        this.daysRecommended = daysRecommended;
    }
}
