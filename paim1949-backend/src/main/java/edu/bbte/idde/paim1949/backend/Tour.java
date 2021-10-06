package edu.bbte.idde.paim1949.backend;

public class Tour {
    public enum SignShape { CIRCLE, TRIANGLE, LINE }

    public enum SignColour { RED, BLUE, YELLOW }

    private float distanceInKm;
    private int elevationInM;
    private SignShape signShape;
    private SignColour signColour;
    private int daysRecommended;

    public Tour(
            float distanceInKm, int elevationInM,
            SignShape signShape, SignColour signColour,
            int daysRecommended) {
        this.distanceInKm = distanceInKm;
        this.elevationInM = elevationInM;
        this.signShape = signShape;
        this.signColour = signColour;
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

    public float getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(float distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public int getElevationInM() {
        return elevationInM;
    }

    public void setElevationInM(int elevationInM) {
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

    public int getDaysRecommended() {
        return daysRecommended;
    }

    public void setDaysRecommended(int daysRecommended) {
        this.daysRecommended = daysRecommended;
    }
}
