package PatientService.file_utils;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Images {
    private static File fileHospital = new File("Images/hospital.png");
    public static Image hospital = new Image(fileHospital.toURI().toString(), 45, 45, true, true);

    private static File fileMonument1 = new File("Images/monument1.png");
    private static final Image monument1 = new Image(fileMonument1.toURI().toString(), 45, 45, true, true);
    private static File fileMonument2 = new File("Images/monument2.png");
    private static final Image monument2 = new Image(fileMonument2.toURI().toString(), 60, 60, true, true);
    private static File fileMonument3 = new File("Images/monument3.png");
    private static final Image monument3 = new Image(fileMonument3.toURI().toString(), 70, 70, true, true);
    private static File fileMonument4 = new File("Images/monument4.png");
    private static final Image monument4 = new Image(fileMonument4.toURI().toString(), 45, 45, true, true);
    public static final List<Image> monuments = new ArrayList<>(Arrays.asList(monument1, monument2, monument3, monument4));

    private static File filePatient1 = new File("Images/patient1.png");
    private static final Image patient1 = new Image(filePatient1.toURI().toString(), 40, 40, true, true);
    private static File filePatient2 = new File("Images/patient2.png");
    private static final Image patient2 = new Image(filePatient2.toURI().toString(), 40, 40, true, true);
    private static File filePatient3 = new File("Images/patient3.png");
    private static final Image patient3 = new Image(filePatient3.toURI().toString(), 40, 40, true, true);
    private static File filePatient4 = new File("Images/patient4.png");
    private static final Image patient4 = new Image(filePatient4.toURI().toString(), 70, 70, true, true);
    private static File filePatientOut = new File("Images/patientInWater.png");
    public static final Image patientOut = new Image(filePatientOut.toURI().toString(), 70, 70, true, true);
    public static final List<Image> patients = new ArrayList<>(Arrays.asList(patient1, patient2, patient3, patient4));

    private static File fileAL1 = new File("Images/karetka1_lewo.png");
    private static final Image ambulance1Left = new Image(fileAL1.toURI().toString(), 50, 34, true, true);
    private static File fileAR1 = new File("Images/karetka1_prawo.png");
    private static final Image ambulance1Right = new Image(fileAR1.toURI().toString(), 50, 34, true, true);
    private static File fileAL2 = new File("Images/karetka2_lewo.png");
    private static final Image ambulance2Left = new Image(fileAL2.toURI().toString(), 50, 34, true, true);
    private static File fileAR2 = new File("Images/karetka2_prawo.png");
    private static final Image ambulance2Right = new Image(fileAR2.toURI().toString(), 50, 34, true, true);
    public static final List<Image> ambulancesLeft = new ArrayList<>(Arrays.asList(ambulance1Left, ambulance2Left));
    public static final List<Image> ambulancesRight = new ArrayList<>(Arrays.asList(ambulance1Right, ambulance2Right));
}