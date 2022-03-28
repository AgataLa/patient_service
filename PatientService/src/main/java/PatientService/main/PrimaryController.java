package PatientService.main;

import PatientService.algorithms.DA;
import PatientService.algorithms.MapAlgorithms;
import PatientService.file_utils.DataVerification;
import PatientService.file_utils.FileServiceInfrastructure;
import PatientService.file_utils.FileServicePatient;
import PatientService.file_utils.Images;
import PatientService.graph.Node;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Patient;
import PatientService.models.Road;
import PatientService.patient_transit.AmbulanceService;
import PatientService.patient_transit.NearestHospital;
import PatientService.patient_transit.PatientQueue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PrimaryController implements Initializable {

    @FXML
    public
    Canvas canvas;

    @FXML
    TextArea textArea;

    @FXML
    TextField mapFile, patientsFile, singlePatient;

    @FXML
    VBox rightVBox, leftVBox;

    @FXML
    HBox mainPane;

    @FXML
    Label messagesTitle;

    @FXML
    Slider speedSlider;

    @FXML
    CheckBox checkBoxMonuments, checkBoxHospitals, checkBoxRoads;

    volatile PrintStream ps;
    Map<Integer, Hospital> hospitals;
    Map<Integer, Monument> monuments;
    Map<Integer, Road> roads;
    Map<Integer, Patient> patients;
    FileServiceInfrastructure fsi;
    FileServicePatient fsp;
    NearestHospital nh;
    Stage stage;
    MapAlgorithms ma;
    public boolean isMapLoaded = false;
    public double[][] mapPoints;

    List<Node> hospital;
    List<Node> monument;
    List<Node> hull;

    volatile List<Patient> patientList;
    PatientQueue<Patient> patientQueue;
    AmbulanceService as;
    DA da;

    volatile static double speed;
    static Object pause = new Object();

    Random rand;

    @FXML
    private void readMap() {
        if (GraphicalUserInterface.isPatient) {
            System.out.println("Nie można wczytać nowej mapy podczas obsługi pacjentów.");
            return;
        }
        hospitals.clear();
        monuments.clear();
        roads.clear();
        String path = mapFile.getText();

        if (fsi.readData(path, hospitals, monuments, roads) == 0) {
            hull = ma.calculateConvexHull(monuments, hospitals);
            if (hull != null) {
                mapPoints = ma.calculateMapCoordinates(hull, canvas.getWidth(), canvas.getHeight());

                hospital = new ArrayList<>(hospitals.values());
                monument = new ArrayList<>(monuments.values());
                ma.calculateCoordinates(hospital);
                ma.calculateCoordinates(monument);

                nh = new NearestHospital(hospitals);
                da = new DA(roads, hospitals, ma);
                as.setNH(nh);
                as.setDA(da);
                isMapLoaded = true;
            } else {
                isMapLoaded = false;
            }
        } else {
            isMapLoaded = false;
        }
    }

    public void recalculate() {
        if (isMapLoaded) {
            mapPoints = ma.calculateMapCoordinates(hull, canvas.getWidth(), canvas.getHeight());
            ma.calculateCoordinates(hospital);
            ma.calculateCoordinates(monument);
        }
    }

    @FXML
    private void readPatients() {
        patients.clear();
        String path = patientsFile.getText();

        if (isMapLoaded) {
            if (fsp.readData(path, patients) == 0) {

                patientList = new ArrayList<>(patients.values());
                ma.calculateCoordinates(patientList);
                Patient p;
                for (int i = 0; i < patientList.size(); i++) {
                    p = patientList.get(i);
                    p.setInPolygon(ma.isPointInPolygon(hull, p));
                    if (p.isInPolygon()) {
                        p.setImage(Images.patients.get(rand.nextInt(4)));
                    } else {
                        p.setImage(Images.patientOut);
                    }
                }
                patientQueue.putAll(patientList);
            }
        } else {
            System.out.println("Najpierw należy wczytać mapę.");
        }
    }

    List<Patient> onePatient;

    @FXML
    private void readPatient() {
        String path = singlePatient.getText();

        if (path.isEmpty()) {
            System.out.println("Proszę podać współrzędne nowego pacjenta.");
            return;
        }

        List<String> data = Arrays.asList(path.split(" "));
        boolean correct = true;

        if (data.size() != 2) {
            System.out.println("Nieprawidłowa liczba danych.");
            correct = false;
        }

        double x = 0, y = 0;
        DataVerification df = new DataVerification();

        if(correct) {
            if (df.isDouble(data.get(0))) {
                x = Double.parseDouble(data.get(0));
            } else {
                System.out.println("Nieprawidłowa współrzędna x.");
                correct = false;
            }

            if (df.isDouble(data.get(1))) {
                y = Double.parseDouble(data.get(1));
            } else {
                System.out.println("Nieprawidłowa współrzędna y.");
                correct = false;
            }
        }

        if (correct) {
            if (isMapLoaded) {
                System.out.println("Nowy pacjent w kolejce.");
                onePatient = new ArrayList<>();
                Patient patient = new Patient(-1, x, y);
                onePatient.add(patient);
                ma.calculateCoordinates(onePatient);
                patient.setInPolygon(ma.isPointInPolygon(hull, patient));

                if (patient.isInPolygon()) {
                    patient.setImage(Images.patients.get(rand.nextInt(4)));
                } else {
                    patient.setImage(Images.patientOut);
                }
                patientQueue.put(patient);
            } else {
                System.out.println("Najpierw należy wczytać mapę.");
            }
        }
    }

    public boolean hideHospital = false;

    @FXML
    private void hideHospitals() {
        if (checkBoxHospitals.isSelected() == true) {
            hideHospital = true;
            System.out.println("Szpitale zostały ukryte.");
        } else {
            hideHospital = false;
            System.out.println("Szpitale zostały uwidocznione.");
        }
    }

    public boolean hideMonument = false;

    @FXML
    private void hideMonuments() {
        if (checkBoxMonuments.isSelected() == true) {
            hideMonument = true;
            System.out.println("Monumenty zostały ukryte.");
        } else {
            hideMonument = false;
            System.out.println("Monumenty zostały uwidocznione.");
        }
    }

    public boolean hideRoad = false;

    @FXML
    private void hideRoads() {
        if (checkBoxRoads.isSelected() == true) {
            hideRoad = true;
            System.out.println("Drogi zostały ukryte.");
        } else {
            hideRoad = false;
            System.out.println("Drogi zostały uwidocznione.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fsi = new FileServiceInfrastructure();
        fsp = new FileServicePatient();
        ma = new MapAlgorithms();
        patientQueue = new PatientQueue();
        rand = new Random();

        as = new AmbulanceService(patientQueue);
        as.start();

        rightVBox.prefHeightProperty().bind(mainPane.heightProperty());
        rightVBox.prefWidthProperty().bind(mainPane.widthProperty());

        textArea.setWrapText(true);
        textArea.setEditable(false);

        messagesTitle.prefWidthProperty().bind(rightVBox.widthProperty());
        textArea.prefWidthProperty().bind(rightVBox.widthProperty());

        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.valueProperty().setValue(1);
        speed = 1.0;
        speedSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            speed = newValue.doubleValue();
            as.getTimeline().setRate(speed);
            if (oldValue.doubleValue() == 0 && newValue.doubleValue() > 0) {
                synchronized (pause) {
                    pause.notifyAll();
                }
            }
        });

        canvas.setWidth(GraphicalUserInterface.WIDTH / 2 - 300);
        canvas.setHeight(GraphicalUserInterface.HEIGHT / 2 - 245);

        ps = new PrintStream(new MyConsole(textArea), true, StandardCharsets.UTF_8);
        System.setOut(ps);
    }

    public void setPatients(Map<Integer, Patient> patients) {
        this.patients = patients;
    }

    public void setMonuments(Map<Integer, Monument> monuments) {
        this.monuments = monuments;
    }

    public void setHospitals(Map<Integer, Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public void setRoads(Map<Integer, Road> roads) {
        this.roads = roads;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static double getSpeed() {
        return speed;
    }

    public static Object getPause() {
        return pause;
    }

    private class MyConsole extends OutputStream {
        private volatile TextArea console;

        public MyConsole(TextArea console) {
            this.console = console;
        }

        public synchronized void appendText(String valueOf) {
            String lines[] = console.getText().split("\n");

            if (lines.length > 20) {
                String textToSet = "";
                for (int i = 1; i < lines.length; i++) {
                    textToSet += lines[i];
                    textToSet += "\n";
                }
                console.setText(textToSet);
            }
            console.appendText(valueOf);
            console.setScrollTop(console.getCaretPosition());
        }

        @Override
        public void write(int b) {
            appendText(String.valueOf((char) b));
        }

        @Override
        public void write(byte[] b, int off, int len) {
            appendText(new String(b, off, len));
        }
    }
}