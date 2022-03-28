package PatientService.models;

import PatientService.graph.Node;

import java.util.Objects;

public class Monument extends Node {

    private final int monumentId;
    private final String monumentName;
    private double monumentX;
    private double monumentY;
    private int image;

    public Monument(int monumentId, String monumentName, double monumentX, double monumentY) {
        this.monumentId = monumentId;
        this.monumentName = monumentName;
        this.monumentX = monumentX;
        this.monumentY = monumentY;
    }

    public int getMonumentId() {
        return monumentId;
    }

    public double getMonumentX() {
        return monumentX;
    }

    public double getMonumentY() {
        return monumentY;
    }

    public int getNrImage() {
        return image;
    }

    public void setNrImage(int image) {
        this.image = image;
    }

    @Override
    public double getX() {
        return monumentX;
    }

    @Override
    public double getY() {
        return monumentY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monument)) return false;
        Monument monument = (Monument) o;
        return getMonumentId() == monument.getMonumentId() &&
                Double.compare(monument.getMonumentX(), getMonumentX()) == 0 &&
                Double.compare(monument.getMonumentY(), getMonumentY()) == 0 &&
                Objects.equals(monumentName, monument.monumentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMonumentId(), monumentName, getMonumentX(), getMonumentY());
    }
}