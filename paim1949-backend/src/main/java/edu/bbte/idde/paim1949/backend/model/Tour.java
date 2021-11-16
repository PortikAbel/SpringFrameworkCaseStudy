package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
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
    @IgnoreColumn
    private Long regionId;
    @IgnoreColumn
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

    public String getSignShape() {
        return signShape.name();
    }

    public void setSignShape(String signShape) {
        this.signShape = SignShape.valueOf(signShape);
    }

    public String getSignColour() {
        return signColour.name();
    }

    public void setSignColour(String signColour) {
        this.signColour = SignColour.valueOf(signColour);
    }
}
