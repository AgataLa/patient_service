package PatientService.models;

import PatientService.graph.Node;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;

import java.util.Objects;

public class Patient extends Node {

    private final int patientId;
    private double patientX;
    private double patientY;
    private boolean isInPolygon;
    private Image image;
    private SimpleDoubleProperty dx;
    private SimpleDoubleProperty dy;

    public Patient(int patientId, double patientX, double patientY) {
        this.patientId = patientId;
        this.patientX = patientX;
        this.patientY = patientY;
    }

    public int getPatientId() {
        return patientId;
    }

    public double getPatientX() {
        return patientX;
    }

    public double getPatientY() {
        return patientY;
    }

    public boolean isInPolygon() {
        return isInPolygon;
    }

    public Image getImage() {
        return image;
    }

    public SimpleDoubleProperty getDx() {
        return dx;
    }

    public SimpleDoubleProperty getDy() {
        return dy;
    }

    public void setInPolygon(boolean inPolygon) {
        isInPolygon = inPolygon;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public double getX() {
        return patientX;
    }

    @Override
    public double getY() {
        return patientY;
    }

    @Override
    public void setxCoor(double xCoor) {
        super.setxCoor(xCoor);
        dx = new SimpleDoubleProperty(xCoor);
    }

    @Override
    public void setyCoor(double yCoor) {
        super.setyCoor(yCoor);
        dy = new SimpleDoubleProperty(yCoor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return getPatientId() == patient.getPatientId() &&
                Double.compare(patient.getPatientX(), getPatientX()) == 0 &&
                Double.compare(patient.getPatientY(), getPatientY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPatientId(), getPatientX(), getPatientY());
    }
}