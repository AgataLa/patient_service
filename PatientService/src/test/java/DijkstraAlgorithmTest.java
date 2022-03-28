import PatientService.algorithms.DA;
import PatientService.algorithms.MapAlgorithms;
import PatientService.file_utils.FileServiceInfrastructure;
import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Road;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DijkstraAlgorithmTest {

    private FileServiceInfrastructure fileServiceInfrastructure;
    private Map<Integer, Hospital> hospitals;
    private Map<Integer, Monument> monuments;
    private Map<Integer, Road> roads;
    private MapAlgorithms ma;

    @Before
    public void setUp() {
        fileServiceInfrastructure = new FileServiceInfrastructure();
        hospitals = new HashMap<>();
        monuments = new HashMap<>();
        roads = new HashMap<>();
        ma = new MapAlgorithms();
    }

    @Test
    public void shouldFindShortestPath_toNodes() {
        fileServiceInfrastructure.readData("infrastructure.txt", hospitals, monuments, roads);

        DA dijkstra = new DA(roads, hospitals, ma);
        List<Node> a = dijkstra.findNextHospital(hospitals.get(1));

        for (int i = 0; i < a.size(); i++)
            System.out.println(a.get(i).getId());
    }
}