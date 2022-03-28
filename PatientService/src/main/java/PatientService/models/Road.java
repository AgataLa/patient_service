package PatientService.models;

import PatientService.graph.Edge;
import PatientService.graph.Node;

import java.util.ArrayList;
import java.util.Objects;

public class Road extends Edge {

    private final int firstHospitalId;
    private final int secondHospitalId;
    private final int roadId;
    private final double distance;
    ArrayList<Intersection> intersections;

    public Road(int firstHospitalId, int secondHospitalId, int roadId, double distance) {
        this.firstHospitalId = firstHospitalId;
        this.secondHospitalId = secondHospitalId;
        this.roadId = roadId;
        this.distance = distance;
    }

    public int getFirstHospitalId() {
        return firstHospitalId;
    }

    public int getSecondHospitalId() {
        return secondHospitalId;
    }

    public int getRoadId() {
        return roadId;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        Road road = (Road) o;
        return getFirstHospitalId() == road.getFirstHospitalId() &&
                getSecondHospitalId() == road.getSecondHospitalId() &&
                getRoadId() == road.getRoadId() &&
                Double.compare(road.getDistance(), getDistance()) == 0 &&
                Objects.equals(intersections, road.intersections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstHospitalId(), getSecondHospitalId(), getRoadId(), getDistance(), intersections);
    }

    @Override
    public Node getFirstNode() {
        return super.getFirstNode();
    }

    @Override
    public Node getSecondNode() {
        return super.getSecondNode();
    }

    @Override
    public double getCost() {
        return distance;
    }
}