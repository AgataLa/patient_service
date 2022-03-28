import PatientService.file_utils.DataVerification;
import PatientService.models.Hospital;
import PatientService.models.Patient;
import PatientService.models.Road;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataVerificationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Map<Integer, Hospital> hospitals;
    private Map<Integer, Road> roads;
    private Map<Integer, Patient> patients;
    private DataVerification dataVerification = new DataVerification();

    @Before
    public void setUp() {
        hospitals = new HashMap<>();
        roads = new HashMap<>();
        patients = new HashMap<>();
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void checkIfLineIsEmpty() {
        String emptyLine = "";

        boolean isEmpty = dataVerification.isEmpty(emptyLine, 1);

        assertEquals(true, isEmpty);

    }

    @Test
    public void checkNumberOfSpace() {
        try {
            dataVerification.checkPatient(1, "1 | 20  20", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 nie występuje poprawna liczba [ | ].", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkDataSize() {
        try {
            dataVerification.checkPatient(1, "1 | 5 | ", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 nie występuje poprawna liczba danych.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkDataLength() {
        try {
            dataVerification.checkRoad(1, "1 |  | 2 | 700", roads, hospitals);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 nie występuje poprawna liczba danych.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkNegativeID() {
        try {
            dataVerification.checkPatient(1, "-1 | 5 | 10", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 id jest ujemne.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkNonIntegerID() {
        try {
            dataVerification.checkPatient(1, "a | 5 | 10", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 id jest nieprawidłowe.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkIfID_isRepeated() {
        Patient patient1 = new Patient(1, 20, 20);
        Patient patient2 = new Patient(1, 99, 105);
        patients.put(1, patient1);
        patients.put(2, patient2);

        dataVerification.checkPatient(1, "1 | 20 | 20", patients);

        assertEquals("Błędne dane - powtórzenie id.", outContent.toString().trim());
    }

    @Test
    public void checkCoordinates() {
        try {
            dataVerification.checkPatient(1, "1 | a | 10", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 współrzędna jest nieprawidłowa.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkNegativeNumberOfBeds() {
        try {
            dataVerification.checkHospital(1, "1 | Szpital Wojewódzki nr 997 | 10 | 10 | b | 100", hospitals);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 liczba łóżek jest nieprawidłowa.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkNonIntegerNumberOfBeds() {
        try {
            dataVerification.checkHospital(1, "1 | Szpital Wojewódzki nr 997 | 10 | 10 | 20 | -100", hospitals);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 liczba łóżek jest ujemna.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkIfHospitalExists() {
        Hospital hospital1 = new Hospital(1, "Szpital Wojewódzki nr 997", 10.0, 10.0, 1000, 100);
        hospitals.put(1, hospital1);

        dataVerification.checkRoad(1, "1 | 1 | 2 | 700", roads, hospitals);

        assertEquals("Linia numer: 1. Nie istnieje taki szpital.", outContent.toString().trim());

    }

    @Test
    public void checkNegativeDistance() {
        try {
            dataVerification.checkRoad(1, "1 | 1 | 2 | -80", roads, hospitals);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 odległość jest ujemna.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void checkNonIntegerDistance() {
        try {
            dataVerification.checkPatient(1, "a | 5 | 10", patients);
        } catch (NumberFormatException e) {
            assertEquals("W linii numer 1 id jest nieprawidłowe.", e.getMessage());
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void checkIfFreeBedsAmount_isLessThanTotalBedsAmount() {
        dataVerification.checkHospital(1, "1 | Szpital Wojewódzki nr 997 | 10 | 10 | 100 | 200", hospitals);
        assertEquals("Błąd - liczba wolnych łóżek jest większa od liczby wszystkich łóżek.", outContent.toString().trim());
    }

    @Test
    public void checkIfRoadAlreadyExists() {
        Road road1 = new Road(1, 2, 1, 700);
        Road road2 = new Road(1, 4, 2, 550);
        Road road3 = new Road(1, 4, 2, 550);
        roads.put(1, road1);
        roads.put(2, road2);
        roads.put(3, road3);

        dataVerification.checkConnections(roads);

        assertEquals("Powtórzenie drogi między szpitalami. Usunięcie nadmiarowej drogi.", outContent.toString().trim());
    }

    @Test
    public void checkIfRoad_isFromAndToTheSameHospital() {
        Road road1 = new Road(1, 1, 1, 700);
        roads.put(1, road1);

        dataVerification.checkConnections(roads);

        assertEquals("Droga z i do tego samego szpitala. Usunięcie drogi.", outContent.toString().trim());
    }

    @Test
    public void checkRepetitionHospitalCoordinates() {
        Hospital hospital1 = new Hospital(1, "A", 10, 10, 200, 10);
        Hospital hospital2 = new Hospital(2, "B", 10, 10, 400, 5);
        hospitals.put(1, hospital1);
        hospitals.put(2, hospital2);

        dataVerification.checkRepetitionHospitalCoordinates(hospitals);

        assertEquals("Błąd - dwa szpitale mają takie same współrzędne.", outContent.toString().trim());
    }
}