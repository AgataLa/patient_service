package PatientService.patient_transit;

import PatientService.algorithms.DA;
import PatientService.file_utils.Images;
import PatientService.graph.Node;
import PatientService.main.GraphicalUserInterface;
import PatientService.main.PrimaryController;
import PatientService.models.Hospital;
import PatientService.models.Patient;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class AmbulanceService extends Service<Boolean> {

    PatientQueue<Patient> patients;
    NearestHospital nh;
    Patient patient;
    Task<Boolean> task;
    Timeline timeline = new Timeline();
    DA da;
    Node current;

    public AmbulanceService(PatientQueue<Patient> patients) {
        this.patients = patients;
    }

    @Override
    protected Task<Boolean> createTask() {
        task = new Task<>() {

            private Hospital hospital;
            private boolean served = false;
            private boolean inQueue = false;
            private volatile boolean finished = false;
            private final int velocity = 11;
            private Random rand = new Random();
            private double timeIn, timeOut;
            private List<Node> min;

            @Override
            protected Boolean call() throws Exception {
                while (true) {
                    GraphicalUserInterface.isPatient = false;
                    try {
                        patient = patients.get();
                    } catch (InterruptedException e) {
                        System.out.println("Problem z obsługą pacjenta.");
                        continue;
                    }
                    GraphicalUserInterface.displayPatient = patient;
                    GraphicalUserInterface.isPatient = true;
                    finished = false;
                    inQueue = false;
                    GraphicalUserInterface.displayPatient = patient;

                    try {
                        timeIn = 1000.0 / PrimaryController.getSpeed();
                        timeOut = 1500.0 / PrimaryController.getSpeed();
                        if (timeIn > 4000.0) {
                            timeIn = 4000.0;
                            timeOut = 4000.0;
                        }
                    } catch (ArithmeticException e) {
                        timeIn = 0;
                        timeOut = 0;
                    }

                    if (patient.isInPolygon()) {
                        if (PrimaryController.getSpeed() != 0) {
                            Thread.sleep((long) timeIn);
                        } else {
                            while (PrimaryController.getSpeed() == 0) {
                                synchronized (PrimaryController.getPause()) {
                                    PrimaryController.getPause().wait();
                                }
                            }
                        }
                        hospital = nh.firstNearestHospital(patient);

                        double time = calculateTime(patient, hospital);
                        int im = Math.abs(rand.nextInt() % 2);

                        if (time != 0) {
                            setAmbulanceImage(patient, hospital, im);
                            System.out.println("Karetka odebrała pacjenta i zawozi go do pierwszego najbliższego szpitala - " + hospital.getHospitalName() + ".");
                        } else {
                            System.out.println("W szpitalu " + hospital.getHospitalName() + " zachorował lekarz.");
                        }

                        timeline = new Timeline(
                                new KeyFrame(Duration.millis(time),
                                        new KeyValue(patient.getDx(), hospital.getxCoor()), new KeyValue(patient.getDy(), hospital.getyCoor())
                                ));

                        timeline.setRate(PrimaryController.getSpeed());

                        if (time != 0) {
                            timeline.play();
                        } else {
                            finished = true;
                        }

                        timeline.setOnFinished(actionEvent -> finished = true);
                        while (!finished) {
                        }
                        served = hospital.hasRoom();

                        current = hospital;
                        int i = 1;
                        if (!served) {

                            Node next;
                            try {
                                min = da.findNextHospital(current);
                            } catch (NullPointerException e) {
                                System.out.println("Brak drogi.");
                                served = true;
                                hospital = null;
                            }

                            while (!served) {
                                finished = false;
                                if (hospital != null) {
                                    System.out.println("W szpitalu " + hospital.getHospitalName() + " nie ma wolnych łóżek.");
                                }
                                next = min.get(i);

                                try {
                                    hospital = (Hospital) next;
                                } catch (ClassCastException e) {
                                    hospital = null;
                                }
                                time = calculateTime(patient, next);
                                setAmbulanceImage(patient, next, im);

                                timeline = new Timeline(
                                        new KeyFrame(Duration.millis(time),
                                                new KeyValue(patient.getDx(), next.getxCoor()), new KeyValue(patient.getDy(), next.getyCoor())
                                        ));

                                timeline.setRate(PrimaryController.getSpeed());

                                if (time != 0) {
                                    timeline.play();
                                } else {
                                    finished = true;
                                }
                                timeline.setOnFinished(actionEvent -> finished = true);

                                if (hospital != null) {
                                    served = hospital.hasRoom();
                                }
                                current = next;
                                i++;

                                while (!finished) {
                                }

                                if (i > min.size() - 1 && !served) {
                                    if (hospital != null) {
                                        System.out.println("Brak wolnych łóżek. Karetka z pacjentem stoi w kolejce do szpitala " + hospital.getHospitalName() + ".");
                                        hospital.addToQueue(patient);
                                    }
                                    served = true;
                                    inQueue = true;
                                }
                            }
                        }

                        if (!inQueue) {
                            if (hospital != null) {
                                System.out.println("Pacjent przyjęty w szpitalu " + hospital.getHospitalName() + ".");
                                hospital.decreaseFreeBedsAmount();
                            }
                        }
                    } else {
                        System.out.println("Pacjent poza granicami kraju.");
                        if (PrimaryController.getSpeed() != 0) {
                            Thread.sleep((long) timeOut);
                        } else {
                            while (PrimaryController.getSpeed() == 0) {
                                synchronized (PrimaryController.getPause()) {
                                    PrimaryController.getPause().wait();
                                }
                            }
                        }
                    }
                }
            }

            private void setAmbulanceImage(Patient patient, Node hospital, int im) {
                boolean toLeft = (patient.getDx().doubleValue() - hospital.getxCoor()) > 0;
                if (toLeft) {
                    patient.setImage(Images.ambulancesLeft.get(im));
                } else {
                    patient.setImage(Images.ambulancesRight.get(im));
                }
            }

            private double calculateTime(Patient p, Node h) {
                double xp = p.getDx().doubleValue();
                double yp = p.getDy().doubleValue();
                double xh = h.getxCoor();
                double yh = h.getyCoor();
                double x = xp - xh, y = yp - yh;
                double dist = Math.sqrt(x * x + y * y);
                double time = dist * velocity;
                return time;
            }
        };

        task.setOnFailed((WorkerStateEvent t) -> {
            System.out.println("Błąd obsługi pacjentów.");
            System.err.println(task.getException());
        });
        return task;
    }

    public void setNH(NearestHospital nh) {
        this.nh = nh;
    }

    public void setDA(DA da) {
        this.da = da;
    }

    public Timeline getTimeline() {
        return timeline;
    }
}