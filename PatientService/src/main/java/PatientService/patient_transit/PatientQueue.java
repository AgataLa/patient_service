package PatientService.patient_transit;

import java.util.ArrayList;
import java.util.List;

public class PatientQueue<Patient> {
    private List<Patient> queue = new ArrayList<>();

    public synchronized void put(Patient patient) {
        queue.add(patient);
        notifyAll();
    }

    public synchronized void putAll(List<Patient> patients) {
        queue.addAll(patients);
        notifyAll();
    }

    public synchronized Patient get() throws InterruptedException {
        while (queue.size() < 1) {
            wait();
        }
        return queue.remove(0);
    }

    public int getSize() {
        return queue.size();
    }
}
