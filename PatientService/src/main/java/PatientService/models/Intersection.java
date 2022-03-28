package PatientService.models;

import PatientService.graph.Node;

import java.util.Objects;

public class Intersection extends Node {

    private final int intersectionId;
    private final int firstRoadId;
    private final int secondRoadId;
    private double intersectionX;
    private double intersectionY;

    public Intersection(int intersectionId, int firstRoadId, int secondRoadId, double intersectionX, double intersectionY) {
        this.intersectionId = intersectionId;
        this.firstRoadId = firstRoadId;
        this.secondRoadId = secondRoadId;
        this.intersectionX = intersectionX;
        this.intersectionY = intersectionY;
    }

    public int getIntersectionId() {
        return intersectionId;
    }

    public int getFirstRoadId() {
        return firstRoadId;
    }

    public int getSecondRoadId() {
        return secondRoadId;
    }

    public double getIntersectionX() {
        return intersectionX;
    }

    public double getIntersectionY() {
        return intersectionY;
    }

    @Override
    public double getX() {
        return intersectionX;
    }

    @Override
    public double getY() {
        return intersectionY;
    }

    @Override
    public Integer getId() {
        return intersectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        if (!super.equals(o)) return false;
        Intersection that = (Intersection) o;
        return getIntersectionId() == that.getIntersectionId() &&
                getFirstRoadId() == that.getFirstRoadId() &&
                getSecondRoadId() == that.getSecondRoadId() &&
                Double.compare(that.getIntersectionX(), getIntersectionX()) == 0 &&
                Double.compare(that.getIntersectionY(), getIntersectionY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIntersectionId(), getFirstRoadId(), getSecondRoadId(), getIntersectionX(), getIntersectionY());
    }
}