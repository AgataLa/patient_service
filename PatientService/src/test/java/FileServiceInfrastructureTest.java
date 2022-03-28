import PatientService.file_utils.FileServiceInfrastructure;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Road;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class FileServiceInfrastructureTest {

    private FileServiceInfrastructure fileServiceInfrastructure;
    private Map<Integer, Hospital> hospitals;
    private Map<Integer, Monument> monuments;
    private Map<Integer, Road> roads;

    @Before
    public void setUp() {
        fileServiceInfrastructure = new FileServiceInfrastructure();
        hospitals = new HashMap<>();
        monuments = new HashMap<>();
        roads = new HashMap<>();
    }


    @Test
    public void checkIfMonumentsAre_LoadedCorrectly() {
        String filePath = "infrastructure.txt";
        fileServiceInfrastructure.readData(filePath, hospitals, monuments, roads);

        Monument monument1 = new Monument(1, "Pomnik Wikipedii", -1.0, 50.0);
        Monument monument2 = new Monument(2, "Pomnik Fryderyka Chopina", 110.0, 55.0);
        Monument monument3 = new Monument(3, "Pomnik Anonimowego Przechodnia", 40.0, 70.0);
        Integer key1 = 1;
        Integer key2 = 2;
        Integer key3 = 3;

        assertTrue(monuments.containsKey(key1));
        assertTrue(monuments.containsKey(key2));
        assertTrue(monuments.containsKey(key3));
        assertTrue(monuments.containsValue(monument1));
        assertTrue(monuments.containsValue(monument2));
        assertTrue(monuments.containsValue(monument3));
    }

    @Test
    public void checkIfHospitalsAre_LoadedCorrectly() {
        String filePath = "infrastructure.txt";
        fileServiceInfrastructure.readData(filePath, hospitals, monuments, roads);

        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10.0, 10.0, 1000, 100);
        Hospital hospital2 = new Hospital(2, "Krakowski Szpital Kliniczny", 100, 120, 999, 99);
        Hospital hospital3 = new Hospital(3, "Pierwszy Szpital im. Prezesa RP", 120, 130, 99, 0);
        Hospital hospital4 = new Hospital(4, "Drugi Szpital im. Naczelnika RP", 10, 140, 70, 1);
        Hospital hospital5 = new Hospital(5, "Trzeci Szpital im. Króla RP", 140, 10, 996, 0);
        HashSet<Integer> expectedKeys = new HashSet<>();
        expectedKeys.add(1);
        expectedKeys.add(2);
        expectedKeys.add(3);
        expectedKeys.add(4);
        expectedKeys.add(5);

        assertTrue(hospitals.keySet().equals(expectedKeys));
        assertTrue(hospitals.containsValue(hospital1));
        assertTrue(hospitals.containsValue(hospital2));
        assertTrue(hospitals.containsValue(hospital3));
        assertTrue(hospitals.containsValue(hospital4));
        assertTrue(hospitals.containsValue(hospital5));

    }

    @Test
    public void checkIfRoadsAre_LoadedCorrectly() {
        String filePath = "infrastructure.txt";
        fileServiceInfrastructure.readData(filePath, hospitals, monuments, roads);

        HashMap<Integer, Road> expectedRoads = new HashMap<Integer, Road>();
        Road road1 = new Road(1, 2, 1, 700);
        Road road2 = new Road(1, 4, 2, 550);
        Road road3 = new Road(1, 5, 3, 800);
        Road road4 = new Road(2, 3, 4, 300);
        Road road5 = new Road(2, 4, 5, 550);
        Road road6 = new Road(3, 5, 6, 600);
        Road road7 = new Road(4, 5, 7, 750);
        expectedRoads.put(1, road1);
        expectedRoads.put(2, road2);
        expectedRoads.put(3, road3);
        expectedRoads.put(4, road4);
        expectedRoads.put(5, road5);
        expectedRoads.put(6, road6);
        expectedRoads.put(7, road7);

        assertTrue(roads.keySet().equals(expectedRoads.keySet()));
        assertTrue(roads.get(1).equals(expectedRoads.get(1)));
        assertTrue(roads.get(2).equals(expectedRoads.get(2)));
        assertTrue(roads.get(3).equals(expectedRoads.get(3)));
        assertTrue(roads.get(4).equals(expectedRoads.get(4)));
        assertTrue(roads.get(5).equals(expectedRoads.get(5)));
        assertTrue(roads.get(6).equals(expectedRoads.get(6)));
        assertTrue(roads.get(7).equals(expectedRoads.get(7)));
    }
}