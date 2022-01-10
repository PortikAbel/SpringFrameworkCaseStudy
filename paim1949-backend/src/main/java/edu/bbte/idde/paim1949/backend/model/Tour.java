package edu.bbte.idde.paim1949.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
