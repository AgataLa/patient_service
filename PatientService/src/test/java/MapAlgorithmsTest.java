import PatientService.algorithms.MapAlgorithms;
import PatientService.file_utils.FileServiceInfrastructure;
import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Patient;
import PatientService.models.Road;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class MapAlgorithmsTest {

    private Map<Integer, Hospital> hospitals;
    private Map<Integer, Patient> patients;
    private Map<Integer, Monument> monuments;
    private MapAlgorithms mapAlgorithms;
    private FileServiceInfrastructure fileServiceInfrastructure;
    private Map<Integer, Road> roads;

    @Before
    public void setUp() {
        mapAlgorithms = new MapAlgorithms();
        hospitals = new HashMap<>();
        monuments = new HashMap<>();
        patients = new HashMap<>();
        roads = new HashMap<>();
        fileServiceInfrastructure = new FileServiceInfrastructure();
    }

    @Test
    public void shouldcalculateConvexHullCorrectly() {
        String filePath = "src/main/resources/data/grahamScanTestData.txt";
        fileServiceInfrastructure.readData(filePath, hospitals, monuments, roads);
        Hospital hospital1 = new Hospital(5, "Trzeci Szpital im. Króla RP", 0, 0, 996, 0);
        Monument monument1 = new Monument(2, "Pomnik Fryderyka Chopina", 3, 1);
        Hospital hospital2 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 4, 4, 70, 1);
        Hospital hospital3 = new Hospital(1, "Szpital Wojewódzki nr 997", 0, 3, 1000, 100);
        ArrayList<Node> expected = new ArrayList<>();
        expected.add(hospital1);
        expected.add(monument1);
        expected.add(hospital2);
        expected.add(hospital3);

        ArrayList<Node> hull = mapAlgorithms.calculateConvexHull(monuments, hospitals);

        assertArrayEquals(expected.toArray(), hull.toArray());
    }

    @Test
    public void should_returnTrue_when_PatientIsInPolygon() {
        List<Node> polygon = new ArrayList<>();
        Patient patient = new Patient(0, 2, 3);

        polygon.add(new Monument(3, "M2", 4, 0));
        polygon.add(new Hospital(2, "H2", 4, 5, 200, 100));
        polygon.add(new Monument(1, "M1", 0, 5));
        polygon.add(new Hospital(0, "H1", 0, 0, 100, 50));

        boolean result = mapAlgorithms.isPointInPolygon(polygon, patient);

        assertTrue(result);
    }

    @Test
    public void should_returnTrue_when_PatientIsOnEdge() {
        List<Node> polygon = new ArrayList<>();
        Patient patient = new Patient(0, 2, 0);

        polygon.add(new Monument(3, "M2", 4, 0));
        polygon.add(new Hospital(2, "H2", 4, 5, 200, 100));
        polygon.add(new Monument(1, "M1", 0, 5));
        polygon.add(new Hospital(0, "H1", 0, 0, 100, 50));

        boolean result = mapAlgorithms.isPointInPolygon(polygon, patient);

        assertTrue(result);
    }

    @Test
    public void should_returnFalse_when_PatientIsOutside() {
        List<Node> polygon = new ArrayList<>();
        Patient patient = new Patient(0, 5, 6);

        polygon.add(new Monument(3, "M2", 4, 0));
        polygon.add(new Hospital(2, "H2", 4, 5, 200, 100));
        polygon.add(new Monument(1, "M1", 0, 5));
        polygon.add(new Hospital(0, "H1", 0, 0, 100, 50));

        boolean result = mapAlgorithms.isPointInPolygon(polygon, patient);

        assertFalse(result);
    }
}





