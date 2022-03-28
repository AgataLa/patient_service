package PatientService.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final List<Node> nodes;
    private List<Edge> edges;

    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void modifyGraph() {
        this.edges = makeGraphUndirected();
    }

    public List<Edge> makeGraph() {
        return makeGraphUndirected();
    }

    private List<Edge> makeGraphUndirected() {
        ArrayList<Edge> undirectedEdged = new ArrayList<Edge>();
        for (int i = 0; i < edges.size(); i++) {
            undirectedEdged.add(edges.get(i));
        }
        for (int i = 0; i < edges.size(); i++) {
            Edge current = edges.get(i);
            double cost = current.getCost();
            Node firstNode = current.getFirstNode();
            Node secondNode = current.getSecondNode();
            Edge revertedEdge = new Edge(secondNode, firstNode, cost);
            if (!edges.contains(revertedEdge)) {
                undirectedEdged.add(revertedEdge);
            }
        }
        return undirectedEdged;
    }
}