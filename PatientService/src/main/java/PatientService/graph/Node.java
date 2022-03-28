package PatientService.graph;

import java.util.Objects;

public class Node {

    private Integer id;
    double x, y;
    private double xCoor;
    private double yCoor;
    private boolean hospital = true;

    public Node() {
    }

    public Node(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getxCoor() {
        return xCoor;
    }

    public double getyCoor() {
        return yCoor;
    }

    public void setxCoor(double xCoor) {
        this.xCoor = xCoor;
    }

    public void setyCoor(double yCoor) {
        this.yCoor = yCoor;
    }

    public boolean isHospital() {
        return hospital;
    }

    public void setIsHospital(boolean hospital) {
        this.hospital = hospital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Double.compare(node.getX(), getX()) == 0 &&
                Double.compare(node.getY(), getY()) == 0 &&
                Double.compare(node.getxCoor(), getxCoor()) == 0 &&
                Double.compare(node.getyCoor(), getyCoor()) == 0 &&
                isHospital() == node.isHospital() &&
                Objects.equals(getId(), node.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getX(), getY(), getxCoor(), getyCoor(), isHospital());
    }
}