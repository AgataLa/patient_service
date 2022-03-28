import PatientService.file_utils.FileServicePatient;
import PatientService.models.Patient;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class FileServicePatientTest {

    private FileServicePatient fileServicePatient;
    private Map<Integer, Patient> patients;

    @Before
    public void setUp() {
        fileServicePatient = new FileServicePatient();
        patients = new HashMap<>();
    }

    @Test
    public void testForFileWithCorrectData() {
        String path = "patient.txt";
        fileServicePatient.readData(path, patients);

        Patient patient1 = new Patient(1, 20, 20);
        Patient patient2 = new Patient(2, 99, 105);
        Patient patient3 = new Patient(3, 23, 40);
        HashSet<Integer> expectedKeys = new HashSet<>();
        expectedKeys.add(1);
        expectedKeys.add(2);
        expectedKeys.add(3);

        assertTrue(patients.keySet().equals(expectedKeys));
        assertTrue(patients.get(1).equals(patient1));
        assertTrue(patients.containsValue(patient2));
        assertTrue(patients.containsValue(patient3));
    }
}