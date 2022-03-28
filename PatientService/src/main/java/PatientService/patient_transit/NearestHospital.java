package PatientService.patient_transit;

import PatientService.models.Hospital;
import PatientService.models.Patient;

import java.util.Map;
import java.util.Set;

public class NearestHospital {

    private Map<Integer, Hospital> hospitals;

    public NearestHospital(Map<Integer, Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public Hospital firstNearestHospital(Patient patient) {
        Set<Integer> keys = hospitals.keySet();
        int[] id_h = keys.stream().mapToInt(Integer::intValue).toArray();

        double distance;
        double x;
        double y;
        Hospital hospital;
        int id = -1;
        double min = Integer.MAX_VALUE;

        for (int i = 0; i < id_h.length; i++) {
            hospital = hospitals.get(id_h[i]);
            x = patient.getPatientX() - hospital.getHospitalX();
            y = patient.getPatientY() - hospital.getHospitalY();
            distance = x * x + y * y;
            if (distance <= min) {
                min = distance;
                id = hospital.getHospitalId();
            }
        }
        return hospitals.get(id);
    }
}

