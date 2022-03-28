package PatientService.algorithms;

import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapAlgorithms {

    private int orientation(Node p, Node c, Node n) {
        double value = (c.getY() - p.getY()) * (n.getX() - c.getX()) -
                (c.getX() - p.getX()) * (n.getY() - c.getY());
        if (value == 0) return 0;
        return (value > 0) ? 1 : 2;
    }


    public ArrayList<Node> calculateConvexHull(Map<Integer, Monument> monuments, Map<Integer, Hospital> hospitals) {
        List<Node> points = new ArrayList(monuments.values());
        List<Node> hospitalValues = new ArrayList(hospitals.values());
        points.addAll(hospitalValues);
        int n = points.size();
        if (n < 3) {
            System.out.println("Należy podać co najmniej trzy pomniki i szpitale.");
            return null;
        }
        ArrayList<Node> hull = new ArrayList<Node>();
        int leftmostPoint = 0;
        for (int i = 1; i < n; i++) {
            if (points.get(i).getX() < points.get(leftmostPoint).getY())
                leftmostPoint = i;
        }
        int currentPoint = leftmostPoint, nextPoint;
        do {
            hull.add(points.get(currentPoint));
            nextPoint = (currentPoint + 1) % n;

            for (int i = 0; i < n; i++) {
                if (orientation(points.get(currentPoint), points.get(i), points.get(nextPoint)) == 2)
                    nextPoint = i;
            }

            currentPoint = nextPoint;

        } while (currentPoint != leftmostPoint);

        return hull;
    }

    public boolean isPointInPolygon(List<Node> polygon, Patient patient) {
        int n = polygon.size();

        double x = patient.getPatientX();
        double y = patient.getPatientY();

        for (int i = 0; i < n; i++) {
            double x1 = polygon.get(i).getX();
            double y1 = polygon.get(i).getY();
            double x2 = polygon.get((i + 1) % n).getX();
            double y2 = polygon.get((i + 1) % n).getY();

            double side = -((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1));

            if (side < 0) {
                return false;
            }
        }
        return true;
    }

    double minx, maxx, miny, maxy;
    double canvasX, canvasY, heightCanvas;

    public double[][] calculateMapCoordinates(List<Node> polygon, double widthCanvas, double heightCanvas) {
        double[][] points = new double[2][];
        points[0] = new double[polygon.size()];
        points[1] = new double[polygon.size()];

        minx = Double.MAX_VALUE;
        maxx = Double.MIN_VALUE;
        miny = Double.MAX_VALUE;
        maxy = Double.MIN_VALUE;

        for (Node node : polygon) {
            minx = Math.min(minx, node.getX());
            maxx = Math.max(maxx, node.getX());
            miny = Math.min(miny, node.getY());
            maxy = Math.max(maxy, node.getY());
        }

        double rangeX = maxx - minx;
        double rangeY = maxy - miny;
        int padding = 30;
        canvasX = widthCanvas - padding * 2;
        canvasY = heightCanvas - padding * 2;
        this.heightCanvas = heightCanvas;

        int it = 0;
        for (Node node : polygon) {
            points[0][it] = (node.getX() - minx) * (canvasX / rangeX) + padding;
            points[1][it++] = heightCanvas - ((node.getY() - miny) * (canvasY / rangeY) + padding);
        }
        return points;
    }

    public void calculateCoordinates(List<? extends Node> objects) {

        double rangeX = maxx - minx;
        double rangeY = maxy - miny;
        int padding = 30;

        for (Node node : objects) {
            node.setxCoor((node.getX() - minx) * (canvasX / rangeX) + padding);
            node.setyCoor(heightCanvas - ((node.getY() - miny) * (canvasY / rangeY) + padding));
        }
    }
}