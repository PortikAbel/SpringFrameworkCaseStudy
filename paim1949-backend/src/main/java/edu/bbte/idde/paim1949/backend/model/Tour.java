package edu.bbte.idde.paim1949.backend.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tour extends BaseEntity {
    public enum SignShape { CIRCLE, TRIANGLE, LINE }

    public enum SignColour { RED, BLUE, YELLOW }

    private Float distanceInKm;
    private Integer elevationInM;
    private SignShape signShape;
    private SignColour signColour;
    private Integer daysRecommended;

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

    public Float getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(Float distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public Integer getElevationInM() {
        return elevationInM;
    }

    public void setElevationInM(Integer elevationInM) {
        this.elevationInM = elevationInM;
    }

    public SignShape getSignShape() {
        return signShape;
    }

    public void setSignShape(SignShape signShape) {
        this.signShape = signShape;
    }

    public SignColour getSignColour() {
        return signColour;
    }

    public void setSignColour(SignColour signColour) {
        this.signColour = signColour;
    }

    public Integer getDaysRecommended() {
        return daysRecommended;
    }

    public void setDaysRecommended(Integer daysRecommended) {
        this.daysRecommended = daysRecommended;
    }

    @Override
    public String toString() {
        return "Tour{"
                + "distanceInKm=" + distanceInKm
                + ", elevationInM=" + elevationInM
                + ", signShape=" + signShape
                + ", signColour=" + signColour
                + ", daysRecommended=" + daysRecommended
                + '}';
    }
}
