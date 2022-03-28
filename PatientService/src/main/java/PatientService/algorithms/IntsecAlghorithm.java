package PatientService.algorithms;

import PatientService.graph.Edge;
import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Intersection;
import PatientService.models.Point;
import PatientService.models.Road;

import java.util.*;

public class IntsecAlghorithm {

    private Map<Integer, Road> roads;
    private Map<Integer, Hospital> hospitals;
    private List<Node> nodes;
    private List<Node> nodeIntersection;
    private List<Edge> onlyHospitals;
    private MapAlgorithms ma;

    public IntsecAlghorithm(Map<Integer, Road> roads, Map<Integer, Hospital> hospitals, MapAlgorithms ma) {
        this.roads = roads;
        this.hospitals = hospitals;
        nodes = new ArrayList<>();
        nodeIntersection = new ArrayList<>();
        onlyHospitals = new ArrayList<>();
        this.ma = ma;
    }

    public List<Edge> getEdges() {
        List<Edge> graph = new ArrayList<>();
        Map<Road, List<Intersection>> intersections = findIntersections();

        Set<Integer> id = roads.keySet();
        int[] id_r = id.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_r.length; i++) {
            int n = intersections.get(roads.get(id_r[i])).size();

            Road road = roads.get(id_r[i]);
            double distance = road.getDistance();

            Hospital hospital1 = hospitals.get(road.getFirstHospitalId());
            Hospital hospital2 = hospitals.get(road.getSecondHospitalId());

            Point p1 = new Point(hospital1.getHospitalX(), hospital1.getHospitalY());
            Point p3 = new Point(hospital2.getHospitalX(), hospital2.getHospitalY());

            double cost;
            double length = distance(p1, p3);

            if (n == 0) {
                Edge edge = new Edge(hospital1, hospital2, distance);
                graph.add(edge);
                onlyHospitals.add(edge);
            } else if (n == 1) {
                Intersection intersection = intersections.get(roads.get(id_r[i])).get(0);
                Point p2 = new Point(intersection.getIntersectionX(), intersection.getIntersectionY());

                cost = cost(p1, p2, length, distance);

                Edge edge1 = new Edge(hospital1, intersection, cost);
                graph.add(edge1);
                Edge edge2 = new Edge(hospital2, intersection, distance - cost);
                graph.add(edge2);
            } else {
                List<Intersection> inter = intersections.get(roads.get(id_r[i]));
                Collections.sort(inter, (i1, i2) -> {
                    if (i1.getIntersectionX() < i2.getIntersectionX()) {
                        return -1;
                    } else if (i1.getIntersectionX() > i2.getIntersectionX()) {
                        return 1;
                    } else {
                        if (i1.getIntersectionY() < i2.getIntersectionY()) {
                            return -1;
                        } else if (i1.getIntersectionY() > i2.getIntersectionY()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                Intersection intersection1 = inter.get(0);
                Point p2 = new Point(intersection1.getIntersectionX(), intersection1.getIntersectionY());
                Intersection intersection2 = inter.get(n - 1);
                Point p4 = new Point(intersection2.getIntersectionX(), intersection2.getIntersectionY());

                List<Hospital> hospitals2 = new ArrayList<>();
                hospitals2.add(hospital1);
                hospitals2.add(hospital2);

                Collections.sort(hospitals2, (h1, h2) -> {
                    if (h1.getHospitalX() < h2.getHospitalX()) {
                        return -1;
                    } else if (h1.getHospitalX() > h2.getHospitalX()) {
                        return 1;
                    } else {
                        if (h1.getHospitalY() < h2.getHospitalY()) {
                            return -1;
                        } else if (h1.getHospitalY() > h2.getHospitalY()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                Hospital h1 = hospitals2.get(0);
                Point ph1 = new Point(h1.getHospitalX(), h1.getHospitalY());

                cost = cost(ph1, p2, length, distance);
                Edge edge1 = new Edge(h1, intersection1, cost);
                graph.add(edge1);

                Hospital h2 = hospitals2.get(1);
                Point ph2 = new Point(h2.getHospitalX(), h2.getHospitalY());

                cost = cost(p4, ph2, length, distance);
                Edge edge2 = new Edge(h2, intersection2, cost);
                graph.add(edge2);

                for (int j = 0; j < n - 1; j++) {
                    int next = j++;
                    Intersection i1 = inter.get(j);
                    Intersection i2 = inter.get(next);
                    Point pi1 = new Point(intersection1.getIntersectionX(), intersection1.getIntersectionY());
                    Point pi4 = new Point(intersection2.getIntersectionX(), intersection2.getIntersectionY());
                    cost = cost(pi1, pi4, length, distance);
                    Edge edge = new Edge(i1, i2, cost);
                    graph.add(edge);
                }
            }
        }
        return graph;
    }

    private double howDivide(Point p1, Point p2, double length) {
        double d;
        double d1 = distance(p1, p2);
        d = d1 / length;
        return d;
    }

    private double distance(Point p1, Point p2) {
        double distance;
        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();
        distance = Math.sqrt(x * x + y * y);
        return distance;
    }

    private double cost(Point p1, Point p2, double length, double distance) {
        double d = howDivide(p1, p2, length);
        double cost = distance * d;
        return cost;
    }

    List<Node> list = new ArrayList<>();

    private Map<Road, List<Intersection>> findIntersections() {
        Map<Road, List<Intersection>> road = new HashMap<>();
        int n = 0;

        Set<Integer> id = roads.keySet();
        int[] id_r = id.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_r.length; i++) {
            List<Intersection> inter = new ArrayList<>();
            road.put(roads.get(id_r[i]), inter);
        }

        for (int i = 0; i < id_r.length - 1; i++) {
            Road road1 = roads.get(id_r[i]);
            for (int j = i + 1; j < id_r.length; j++) {
                Road road2 = roads.get(id_r[j]);
                Point p1 = new Point(hospitals.get(road1.getFirstHospitalId()).getHospitalX(), hospitals.get(road1.getFirstHospitalId()).getHospitalY());
                Point p2 = new Point(hospitals.get(road1.getSecondHospitalId()).getHospitalX(), hospitals.get(road1.getSecondHospitalId()).getHospitalY());
                Point p3 = new Point(hospitals.get(road2.getFirstHospitalId()).getHospitalX(), hospitals.get(road2.getFirstHospitalId()).getHospitalY());
                Point p4 = new Point(hospitals.get(road2.getSecondHospitalId()).getHospitalX(), hospitals.get(road2.getSecondHospitalId()).getHospitalY());

                if (intersectionExist(p1, p2, p3, p4)) {
                    --n;
                    Point p = intersectionCoordinates(p1, p2, p3, p4);
                    Intersection intersection = new Intersection(n, road1.getRoadId(), road2.getRoadId(), p.getX(), p.getY());
                    list.add(intersection);
                    ma.calculateCoordinates(list);
                    list.clear();
                    intersection.setIsHospital(false);
                    road.get(road1).add(intersection);
                    road.get(road2).add(intersection);
                    nodes.add(intersection);
                    nodeIntersection.add(intersection);
                }
            }
        }
        return road;
    }

    private void addHospitals() {
        Set<Integer> id = hospitals.keySet();
        int[] id_h = id.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_h.length; i++) {
            nodes.add(hospitals.get(id_h[i]));
        }
    }

    public List<Node> getNodes() {
        addHospitals();
        return nodes;
    }

    public List<Node> getNodeIntersection() {
        return nodeIntersection;
    }

    public List<Edge> getOnlyHospitals() {
        return onlyHospitals;
    }

    private boolean intersectionExist(Point p1, Point p2, Point p3, Point p4) {
        boolean exist = false;

        double s1 = scalarProduct(p1, p3, p2);
        double s2 = scalarProduct(p1, p4, p2);
        double s3 = scalarProduct(p3, p1, p4);
        double s4 = scalarProduct(p3, p2, p4);

        if (((s1 > 0 && s2 < 0) || (s1 < 0 && s2 > 0)) && ((s3 < 0 && s4 > 0) || (s3 > 0 && s4 < 0)))
            exist = true;

        return exist;
    }

    private double scalarProduct(Point p1, Point p2, Point p3) {
        double s;
        s = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p3.getX() - p1.getX()) * (p2.getY() - p1.getY());
        return s;
    }

    private Point intersectionCoordinates(Point p1, Point p2, Point p3, Point p4) {
        double x;
        double y;

        double x12 = p2.getX() - p1.getX();
        double y12 = p2.getY() - p1.getY();
        double x34 = p4.getX() - p3.getX();
        double y34 = p4.getY() - p3.getY();
        double x13 = p3.getX() - p1.getX();
        double y13 = p3.getY() - p1.getY();
        double t1 = (x13 * y34 - y13 * x34) / (x12 * y34 - y12 * x34);

        x = p1.getX() + t1 * x12;
        y = p1.getY() + t1 * y12;

        Point p = new Point(x, y);

        return p;
    }
}