import PatientService.algorithms.IntsecAlghorithm;
import PatientService.algorithms.MapAlgorithms;
import PatientService.file_utils.FileServiceInfrastructure;
import PatientService.graph.Edge;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Road;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class IntsecAlghorithmTest {

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
    public void findNodes() {
        String filePath = "src/main/resources/data/intsecAlghorithmTestData.txt";
        fileServiceInfrastructure.readData(filePath, hospitals, monuments, roads);
        IntsecAlghorithm intsecAlghorithm = new IntsecAlghorithm(roads, hospitals, ma);

        List<Edge> edges = intsecAlghorithm.getEdges();
        int size = edges.size();
        int expectedSize = 8;

        assertEquals(expectedSize, size);
    }
}
