package PatientService.main;

import PatientService.file_utils.Images;
import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Patient;
import PatientService.models.Road;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphicalUserInterface extends Application {

    private static Scene scene;
    private PrimaryController controller;
    Map<Integer, Hospital> hospitals;
    Map<Integer, Monument> monuments;
    Map<Integer, Road> roads;
    Map<Integer, Patient> patients;
    private AnimationTimer timer;
    Dimension screenSize;
    public static int WIDTH;
    public static int HEIGHT;
    private double diffx, diffy;
    public volatile static Patient displayPatient;
    public volatile static boolean isPatient;

    @Override
    public void init() throws Exception {
        super.init();
        monuments = new HashMap<>();
        hospitals = new HashMap<>();
        roads = new HashMap<>();
        patients = new HashMap<>();
        displayPatient = new Patient(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = screenSize.width;
        HEIGHT = screenSize.height;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Patient Service");
        stage.setResizable(false);

        stage.setMinHeight(600);
        stage.setMinWidth(900);
        stage.setHeight(HEIGHT / 2);
        stage.setWidth(WIDTH / 2);

        FXMLLoader fxmlLoader = new FXMLLoader(GraphicalUserInterface.class.getResource("primary.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        controller.setPatients(patients);
        controller.setHospitals(hospitals);
        controller.setMonuments(monuments);
        controller.setRoads(roads);
        controller.setStage(stage);
        diffx = stage.getWidth() - controller.canvas.getWidth();
        diffy = stage.getHeight() - controller.canvas.getHeight();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (!isPatient) {
                controller.canvas.setWidth(stage.getWidth() - diffx);
                controller.recalculate();
            }
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (!isPatient) {
                controller.canvas.setHeight(stage.getHeight() - diffy);
                controller.recalculate();
            }
        });

        scene = new Scene(root);

        timer = new MyTimer();
        timer.start();

        stage.setScene(scene);
        stage.show();
    }

    private class MyTimer extends AnimationTimer {

        @Override
        public void handle(long now) {
            doHandle();
        }

        private void doHandle() {
            draw();
        }
    }

    public void draw() {
        GraphicsContext gc = controller.canvas.getGraphicsContext2D();

        if (controller.isMapLoaded) {
            fillBackground(gc, "#00bfff");

            gc.setFill(Paint.valueOf("#deb887"));
            gc.fillPolygon(controller.mapPoints[0], controller.mapPoints[1], controller.mapPoints[0].length);

            if (!controller.hideRoad) {
                for (Road r : controller.roads.values()) {
                    gc.setStroke(Color.valueOf("#727c8c"));
                    gc.setLineWidth(7);
                    gc.strokeLine(controller.hospitals.get(r.getFirstHospitalId()).getxCoor(),
                            controller.hospitals.get(r.getFirstHospitalId()).getyCoor(),
                            controller.hospitals.get(r.getSecondHospitalId()).getxCoor(),
                            controller.hospitals.get(r.getSecondHospitalId()).getyCoor());
                    gc.setStroke(Color.WHITE);
                    gc.setLineWidth(1);
                    gc.setLineDashes(3);
                    gc.strokeLine(controller.hospitals.get(r.getFirstHospitalId()).getxCoor(),
                            controller.hospitals.get(r.getFirstHospitalId()).getyCoor(),
                            controller.hospitals.get(r.getSecondHospitalId()).getxCoor(),
                            controller.hospitals.get(r.getSecondHospitalId()).getyCoor());
                }
            }

            double xh = Images.hospital.getWidth();
            double yh = Images.hospital.getHeight();
            String freeBeds;

            if (!controller.hideHospital) {
                for (Hospital h : controller.hospitals.values()) {
                    gc.drawImage(Images.hospital, h.getxCoor() - xh / 2, h.getyCoor() - yh / 2, xh, yh);
                    freeBeds = String.valueOf(h.getFreeBedsAmount());
                    gc.setStroke(Color.BLACK);
                    gc.setLineDashes();
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.fillText(freeBeds, h.getxCoor(), h.getyCoor() - yh / 2 - 1);
                    gc.strokeText(freeBeds, h.getxCoor(), h.getyCoor() - yh / 2 - 1);
                    if (h.isQueue()) {
                        int i = 0;
                        for (Patient p : h.getQueue()) {
                            gc.drawImage(p.getImage(), h.getxCoor() + 5 - i * 8, h.getyCoor() + 5, 25, 17);
                            i++;
                        }
                    }
                }
            }

            if (!controller.hideMonument) {
                for (Monument m : controller.monuments.values()) {
                    Image im = Images.monuments.get(m.getNrImage());
                    gc.drawImage(im, m.getxCoor() - im.getWidth() / 2, m.getyCoor() - im.getHeight() / 2, im.getWidth(), im.getHeight());
                }
            }

            if (isPatient) {
                Image im = displayPatient.getImage();
                gc.drawImage(im,
                        displayPatient.getDx().doubleValue() - im.getWidth() / 2,
                        displayPatient.getDy().doubleValue() - im.getHeight() / 2, im.getWidth(), im.getHeight());
            }
        } else {
            fillBackground(gc, "#ffffff");
        }
    }

    private void fillBackground(GraphicsContext gc, String color) {
        gc.setFill(Color.valueOf(color));
        gc.fillRect(0, 0, controller.canvas.getWidth(), controller.canvas.getHeight());
    }
}