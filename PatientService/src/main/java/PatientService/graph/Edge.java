package PatientService.graph;

import java.util.Objects;

public class Edge {

    private Node firstNode;
    private Node secondNode;
    private double cost;

    public Edge(Node firstNode, Node secondNode, double cost) {
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.cost = cost;
    }

    public Edge() {
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getSecondNode() {
        return secondNode;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return Double.compare(edge.getCost(), getCost()) == 0 &&
                Objects.equals(getFirstNode(), edge.getFirstNode()) &&
                Objects.equals(getSecondNode(), edge.getSecondNode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstNode(), getSecondNode(), getCost());
    }
}