package PatientService.file_utils;

import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Patient;
import PatientService.models.Road;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class DataVerification {

    Random rand;

    public DataVerification() {
        rand = new Random();
    }

    public int checkPatient(int lineNr, String line, Map<Integer, Patient> patients) {
        checkSpace(2, lineNr, line);

        List<String> data = Arrays.asList(line.split(" \\| "));
        checkSize(data.size(), 3, lineNr);

        for (int i = 0; i < data.size(); i++) {
            checkLength(data.get(i), lineNr);
        }

        int id = checkId(data.get(0), lineNr);
        if (checkRepetitionId(patients, id) == 1)
            return 1;

        double patientX = checkCoordinate(data.get(1), lineNr);
        double patientY = checkCoordinate(data.get(2), lineNr);

        Patient patient = new Patient(id, patientX, patientY);
        patients.put(id, patient);

        return 0;
    }

    public int checkHospital(int lineNr, String line, Map<Integer, Hospital> hospitals) {
        checkSpace(5, lineNr, line);

        List<String> data = Arrays.asList(line.split(" \\| "));
        checkSize(data.size(), 6, lineNr);

        for (int i = 0; i < data.size(); i++) {
            checkLength(data.get(i), lineNr);
        }

        int id = checkId(data.get(0), lineNr);
        if (checkRepetitionId(hospitals, id) == 1)
            return 1;

        String name = data.get(1);

        double hospitalX = checkCoordinate(data.get(2), lineNr);
        double hospitalY = checkCoordinate(data.get(3), lineNr);

        int beds = checkBeds(data.get(4), lineNr);
        int freeBeds = checkBeds(data.get(5), lineNr);
        if (beds < freeBeds) {
            System.out.println("Błąd - liczba wolnych łóżek jest większa od liczby wszystkich łóżek.");
            return 1;
        }

        Hospital hospital = new Hospital(id, name, hospitalX, hospitalY, beds, freeBeds);
        hospitals.put(id, hospital);

        return 0;
    }

    public int checkMonument(int lineNr, String line, Map<Integer, Monument> monuments) {
        checkSpace(3, lineNr, line);

        List<String> data = Arrays.asList(line.split(" \\| "));
        checkSize(data.size(), 4, lineNr);

        for (int i = 0; i < data.size(); i++) {
            checkLength(data.get(i), lineNr);
        }

        int id = checkId(data.get(0), lineNr);
        if (checkRepetitionId(monuments, id) == 1)
            return 1;

        String name = data.get(1);

        double monumentX = checkCoordinate(data.get(2), lineNr);
        double monumentY = checkCoordinate(data.get(3), lineNr);

        Monument monument = new Monument(id, name, monumentX, monumentY);
        monument.setNrImage(rand.nextInt(4));
        monuments.put(id, monument);

        return 0;
    }

    public int checkRoad(int lineNr, String line, Map<Integer, Road> roads, Map<Integer, Hospital> hospitals) {
        checkSpace(3, lineNr, line);

        List<String> data = Arrays.asList(line.split(" \\| "));
        checkSize(data.size(), 4, lineNr);

        for (int i = 0; i < data.size(); i++) {
            checkLength(data.get(i), lineNr);
        }

        int id = checkId(data.get(0), lineNr);
        if (checkRepetitionId(roads, id) == 1)
            return 1;

        int id1 = checkId(data.get(1), lineNr);
        if (hospitalExist(hospitals, id1, lineNr) == 1)
            return 1;

        int id2 = checkId(data.get(2), lineNr);
        if (hospitalExist(hospitals, id2, lineNr) == 1)
            return 1;

        double distance = checkDistance(data.get(3), lineNr);

        Road road = new Road(id1, id2, id, distance);
        roads.put(id, road);

        return 0;
    }

    public void checkConnections(Map<Integer, Road> roads) {
        List<Integer> id_r = roads.keySet().stream().collect(Collectors.toList());

        for (int i = 0; i < id_r.size(); i++) {
            Road road1 = roads.get(id_r.get(i));
            for (int j = i + 1; j < id_r.size(); j++) {
                Road road2 = roads.get(id_r.get(j));
                if (road1.getFirstHospitalId() == road2.getFirstHospitalId() && road1.getSecondHospitalId() == road2.getSecondHospitalId()) {
                    roads.remove(id_r.get(j));
                    id_r.remove(j);
                    System.out.println("Powtórzenie drogi między szpitalami. Usunięcie nadmiarowej drogi.");
                }
                if (road1.getFirstHospitalId() == road2.getSecondHospitalId() && road1.getSecondHospitalId() == road2.getFirstHospitalId()) {
                    roads.remove(id_r.get(j));
                    id_r.remove(j);
                    System.out.println("Powtórzenie drogi między szpitalami. Usunięcie nadmiarowej drogi.");
                }
            }
            if (road1.getFirstHospitalId() == road1.getSecondHospitalId()) {
                roads.remove(id_r.get(i));
                id_r.remove(i);
                System.out.println("Droga z i do tego samego szpitala. Usunięcie drogi.");
            }
        }
    }

    public boolean isEmpty(String line, int lineNr) {
        boolean empty = false;
        if (line.isEmpty() || line.isBlank()) {
            System.out.println("Linia numer: " + lineNr + " jest pusta.");
            empty = true;
        }
        return empty;
    }

    private void checkSpace(int number, int lineNr, String line) {
        int spaceNr = 0;
        String mark = " \\| ";
        Pattern compiledPattern = Pattern.compile(mark);
        Matcher matcher = compiledPattern.matcher(line);

        while (matcher.find()) {
            spaceNr++;
        }
        if (spaceNr != number)
            throw new NumberFormatException("W linii numer " + lineNr + " nie występuje poprawna liczba [ | ].");
    }

    private void checkSize(int size, int n, int lineNr) {
        if (size != n)
            throw new NumberFormatException("W linii numer " + lineNr + " nie występuje poprawna liczba danych.");
    }

    private void checkLength(String s, int lineNr) {
        if (s.length() == 0)
            throw new NumberFormatException("W linii numer " + lineNr + " nie występuje poprawna liczba danych.");
    }

    private int checkId(String id, int lineNr) {
        int id_p;

        if (!isInteger(id)) {
            throw new NumberFormatException("W linii numer " + lineNr + " id jest nieprawidłowe.");
        } else {
            id_p = Integer.parseInt(id);
            if (checkNegative(id_p)) {
                throw new NumberFormatException("W linii numer " + lineNr + " id jest ujemne.");
            }
        }
        return id_p;
    }

    private double checkCoordinate(String coordinate, int lineNr) {
        double c;

        if (!isDouble(coordinate)) {
            throw new NumberFormatException("W linii numer " + lineNr + " współrzędna jest nieprawidłowa.");
        } else {
            c = Double.parseDouble(coordinate);
        }
        return c;
    }

    private int checkBeds(String beds, int lineNr) {
        int b;

        if (!isInteger(beds)) {
            throw new NumberFormatException("W linii numer " + lineNr + " liczba łóżek jest nieprawidłowa.");
        } else {
            b = Integer.parseInt(beds);
            if (checkNegative(b)) {
                throw new NumberFormatException("W linii numer " + lineNr + " liczba łóżek jest ujemna.");
            }
        }
        return b;
    }

    private double checkDistance(String distance, int lineNr) {
        double d;

        if (!isDouble(distance)) {
            throw new NumberFormatException("W linii numer " + lineNr + " odległość jest nieprawidłowa.");
        } else {
            d = Double.parseDouble(distance);
            if (checkNegative(d)) {
                throw new NumberFormatException("W linii numer " + lineNr + " odległość jest ujemna.");
            }
        }
        return d;
    }

    private int hospitalExist(Map<Integer, Hospital> hospitals, int id, int lineNr) {
        boolean exist = false;

        Set<Integer> id_h = hospitals.keySet();
        int[] id_H = id_h.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_H.length; i++) {
            Hospital hospital = hospitals.get(id_H[i]);
            if (hospital.getHospitalId() == id) {
                exist = true;
            }
        }

        if (!exist) {
            System.out.println("Linia numer: " + lineNr + ". Nie istnieje taki szpital.");
            return 1;
        }
        return 0;
    }

    private boolean checkNegative(int number) {
        return number < 0;
    }

    private boolean checkNegative(double number) {
        return number < 0;
    }

    private boolean isInteger(String dana) {
        try {
            Integer.parseInt(dana);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private int checkRepetitionId(Map elements, int id) {
        if (elements.containsKey(id)) {
            System.out.println("Błędne dane - powtórzenie id.");
            return 1;
        }
        return 0;
    }

    public int checkRepetitionHospitalCoordinates(Map<Integer, Hospital> hospitals) {
        Set<Integer> id_h = hospitals.keySet();
        int[] id_H = id_h.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_H.length; i++) {
            Hospital hospital1 = hospitals.get(id_H[i]);
            for (int j = i + 1; j < id_H.length; j++) {
                Hospital hospital2 = hospitals.get(id_H[j]);
                if (hospital1.getHospitalX() == hospital2.getHospitalX() && hospital1.getHospitalY() == hospital2.getHospitalY()) {
                    System.out.println("Błąd - dwa szpitale mają takie same współrzędne.");
                    return 1;
                }
            }
        }
        return 0;
    }

    public int checkRepetitionMonumentsCoordinates(Map<Integer, Monument> monuments) {
        Set<Integer> id_m = monuments.keySet();
        int[] id_M = id_m.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_M.length; i++) {
            Monument monument1 = monuments.get(id_M[i]);
            for (int j = i + 1; j < id_M.length; j++) {
                Monument monument2 = monuments.get(id_M[j]);
                if (monument1.getMonumentX() == monument2.getMonumentX() && monument1.getMonumentY() == monument2.getMonumentY()) {
                    System.out.println("Błąd - dwa obiekty mają takie same współrzędne.");
                    return 1;
                }
            }
        }
        return 0;
    }

    public int checkRepetitionCoordinates(Map<Integer, Hospital> hospitals, Map<Integer, Monument> monuments) {
        Set<Integer> id_h = hospitals.keySet();
        int[] id_H = id_h.stream().mapToInt(Integer::intValue).toArray();

        Set<Integer> id_m = monuments.keySet();
        int[] id_M = id_m.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < id_H.length; i++) {
            Hospital hospital = hospitals.get(id_H[i]);
            for (int j = 0; j < id_M.length; j++) {
                Monument monument = monuments.get(id_M[j]);
                if (hospital.getHospitalX() == monument.getMonumentX() && hospital.getHospitalX() == monument.getMonumentY()) {
                    System.out.println("Błąd - szpital i obiekt mają takie same współrzędne.");
                    return 1;
                }
            }
        }
        return 0;
    }
}
