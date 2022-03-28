package PatientService.models;

import PatientService.graph.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hospital extends Node {

    private final int hospitalId;
    private final String hospitalName;
    private double hospitalX;
    private double hospitalY;
    private final int bedsAmount;
    private int freeBedsAmount;
    private List<Patient> queue;

    public Hospital(int hospitalId, String hospitalName, double hospitalX, double hospitalY, int bedsAmount, int freeBedsAmount) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalX = hospitalX;
        this.hospitalY = hospitalY;
        this.bedsAmount = bedsAmount;
        this.freeBedsAmount = freeBedsAmount;
        queue = new ArrayList<>();
    }

    public void addToQueue(Patient patient) {
        queue.add(patient);
    }

    public boolean isQueue() {
        return queue.size() > 0;
    }

    public List<Patient> getQueue() {
        return queue;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public int getBedsAmount() {
        return bedsAmount;
    }

    public int getFreeBedsAmount() {
        return freeBedsAmount;
    }

    public void decreaseFreeBedsAmount() {
        freeBedsAmount--;
    }

    public double getHospitalX() {
        return hospitalX;
    }

    public double getHospitalY() {
        return hospitalY;
    }

    public boolean hasRoom() {
        return freeBedsAmount > 0;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    @Override
    public double getX() {
        return hospitalX;
    }

    @Override
    public double getY() {
        return hospitalY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hospital)) return false;
        Hospital hospital = (Hospital) o;
        return getHospitalId() == hospital.getHospitalId() &&
                Double.compare(hospital.getHospitalX(), getHospitalX()) == 0 &&
                Double.compare(hospital.getHospitalY(), getHospitalY()) == 0 &&
                getBedsAmount() == hospital.getBedsAmount() &&
                getFreeBedsAmount() == hospital.getFreeBedsAmount() &&
                Objects.equals(hospitalName, hospital.hospitalName);
    }

    @Override
    public Integer getId() {
        return hospitalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHospitalId(), hospitalName, getHospitalX(), getHospitalY(), getBedsAmount(), getFreeBedsAmount());
    }
}