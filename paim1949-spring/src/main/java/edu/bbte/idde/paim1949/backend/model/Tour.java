package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import lombok.*;
import org.springframework.stereotype.Repository;

@Repository
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tour extends BaseEntity {
    public enum SignShape { CIRCLE, TRIANGLE, LINE, CROSS }

    public enum SignColour { RED, BLUE, YELLOW }

    private Float distanceInKm;
    private Integer elevationInM;
    private SignShape signShape;
    private SignColour signColour;
    private Integer daysRecommended;
    @RefToOne(refTableName = "Region")
    private Long regionId;

    public String getSignShape() {
        return signShape.name();
    }

    public void setSignShape(String signShape) {
        if (signShape != null) {
            this.signShape = SignShape.valueOf(signShape);
        }
    }

    public String getSignColour() {
        return signColour.name();
    }

    public void setSignColour(String signColour) {
        if (signColour != null) {
            this.signColour = SignColour.valueOf(signColour);
        }
    }
}
