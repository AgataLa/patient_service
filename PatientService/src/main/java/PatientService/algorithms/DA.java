package PatientService.algorithms;

import PatientService.graph.Edge;
import PatientService.graph.Graph;
import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Road;

import java.util.*;

public class DA {

    Graph graph;
    private List<Node> nodes;
    private List<Edge> edges;
    private List<Node> nodesCopy;
    private List<Edge> edgesCopy;
    private Map<Integer, Road> roads;
    private Map<Integer, Hospital> hospitals;
    IntsecAlghorithm ia;
    List<Node> i;
    List<Edge> onlyHospitals;
    private List<Node> hosp;

    public DA(Map<Integer, Road> roads, Map<Integer, Hospital> hospitals, MapAlgorithms ma) {
        this.roads = roads;
        this.hospitals = hospitals;

        ia = new IntsecAlghorithm(roads, hospitals, ma);
        List<Edge> e = ia.getEdges();
        List<Node> n = ia.getNodes();

        i = ia.getNodeIntersection();
        onlyHospitals = ia.getOnlyHospitals();

        this.graph = new Graph(n, e);
        graph.modifyGraph();

        this.nodesCopy = new ArrayList<>(graph.getNodes());
        this.edgesCopy = new ArrayList<>(graph.makeGraph());
    }

    public List<Node> findNextHospital(Node start) {
        hosp = new ArrayList<>(hospitals.values());
        nodes = new ArrayList<>(nodesCopy);
        edges = new ArrayList<>(edgesCopy);
        Node current = start;
        hosp.remove(current);
        List<Node> history;

        List<Node> path = new ArrayList<>();

        while (hosp.size() > 0) {
            calculateShortestPathFromSource(current);
            Node next = getMinimumDistanceOfAll(current);

            if (next.isHospital() && hosp.contains(next)) {
                hosp.remove(next);
                history = getPath(next);
                path.addAll(history);
            }
            current = next;
        }

        for (int i = 1; i < path.size(); i++) {
            if (path.get(i).getId() == path.get(i - 1).getId()) {
                path.remove(i);
            }
        }
        return path;
    }

    public LinkedList<Node> getPath(Node target) {
        LinkedList<Node> path = new LinkedList<>();
        Node next = target;
        if (previousNodes.get(next) == null) {
            return null;
        }
        path.add(next);
        while (previousNodes.get(next) != null) {
            next = previousNodes.get(next);
            path.add(next);
        }
        Collections.reverse(path);
        return path;
    }

    public Node getMinimumDistanceOfAll(Node current) {
        HashMap<Node, Double> hmap2 = new HashMap<>();
        hmap2.putAll(distance);
        hmap2.remove(current);
        Map.Entry<Node, Double> min = null;
        for (Map.Entry<Node, Double> entry : hmap2.entrySet()) {
            if ((min == null || min.getValue() > entry.getValue()) && entry.getKey().isHospital() && hosp.contains(entry.getKey())) {
                min = entry;
            }
        }
        return min.getKey();
    }

    private Set<Node> visitedNodes;
    private Set<Node> unvisitedNodes;
    private Map<Node, Node> previousNodes;
    private Map<Node, Double> distance;

    public void calculateShortestPathFromSource(Node source) {
        visitedNodes = new HashSet<>();
        unvisitedNodes = new HashSet<>();
        distance = new HashMap<>();
        previousNodes = new HashMap<>();
        distance.put(source, 0.0);
        unvisitedNodes.add(source);

        while (unvisitedNodes.size() > 0) {
            Node node = getLowestDistanceNode(unvisitedNodes);
            visitedNodes.add(node);
            unvisitedNodes.remove(node);
            calculateMinimumDistance(node);
        }
    }

    private void calculateMinimumDistance(Node start) {
        List<Node> adjacentNodes = getAdjacentNodes(start);
        for (Node next : adjacentNodes) {
            if (getShortestDistance(next) > getShortestDistance(start)
                    + getDistance(start, next)) {
                distance.put(next, getShortestDistance(start)
                        + getDistance(start, next));
                previousNodes.put(next, start);
                unvisitedNodes.add(next);
            }
        }

    }

    private double getDistance(Node node, Node target) {
        for (Edge edge : edges) {
            if (edge.getFirstNode().equals(node)
                    && edge.getSecondNode().equals(target)) {
                return edge.getCost();
            }
        }
        return -1;
    }

    private List<Node> getAdjacentNodes(Node node) {
        List<Node> adjacentNodes = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getFirstNode().equals(node)) {
                adjacentNodes.add(edge.getSecondNode());
            }
        }
        return adjacentNodes;
    }

    private Node getLowestDistanceNode(Set<Node> nodes) {
        Node minimum = null;
        for (Node node : nodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }

    private double getShortestDistance(Node destination) {
        Double d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }
}