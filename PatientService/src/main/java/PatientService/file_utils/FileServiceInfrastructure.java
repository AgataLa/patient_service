package PatientService.file_utils;

import PatientService.models.Hospital;
import PatientService.models.Monument;
import PatientService.models.Road;

import java.io.*;
import java.util.Map;

public class FileServiceInfrastructure {

    public int readData(String path, Map<Integer, Hospital> hospitals, Map<Integer, Monument> monuments, Map<Integer, Road> roads) {

        try {
            if (!path.contains(".txt")) {
                throw new Exception("Podany plik \"" + path + "\" ma niepoprawny format. Plik z danymi o infrastrukturze musi być plikiem tekstowym z rozszerzeniem \".txt\".");
            }
        } catch (NullPointerException ex) {
            System.out.println("Path is null.");
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 1;
        }

        DataVerification dv = new DataVerification();

        File file = new File(path);
        String line;
        int lineNr = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            line = br.readLine();
            line = line.trim();
            lineNr++;

            if (line.contains("# Szpitale")) {
                while (!(line = br.readLine()).trim().startsWith("#")) {
                    lineNr++;
                    if (line.length() != 0) {
                        line = line.trim();
                        if (dv.checkHospital(lineNr, line, hospitals) == 1)
                            return 1;
                    }
                }
            } else {
                throw new Exception("Brak/niepoprawny format nagłówka dla szpitali lub niewłaściwa kolejność nagłówków.");
            }
            if (dv.checkRepetitionHospitalCoordinates(hospitals) == 1)
                return 1;
            lineNr++;
            line = line.trim();
            if (line.contains("# Obiekty")) {
                while (!(line = br.readLine()).trim().startsWith("#")) {
                    lineNr++;
                    if (line.length() != 0) {
                        line = line.trim();
                        if (dv.checkMonument(lineNr, line, monuments) == 1)
                            return 1;
                    }
                }
            } else {
                throw new Exception("Brak/niepoprawny format nagłówka dla obiektów lub niewłaściwa kolejność nagłówków.");
            }
            if (dv.checkRepetitionMonumentsCoordinates(monuments) == 1)
                return 1;
            if (dv.checkRepetitionCoordinates(hospitals, monuments) == 1)
                return 1;
            lineNr++;
            line = line.trim();
            if (line.contains("# Drogi")) {
                while ((line = br.readLine()) != null) {
                    lineNr++;
                    if (line.length() != 0) {
                        line = line.trim();
                        if (dv.checkRoad(lineNr, line, roads, hospitals) == 1)
                            return 1;
                    }
                }
            } else {
                throw new Exception("Brak/niepoprawny format nagłówka dla dróg lub niewłaściwa kolejność nagłówków.");
            }
            dv.checkConnections(roads);
            System.out.println("Udało się wczytać dane z pliku o infrastrukturze.");
        } catch (FileNotFoundException ex) {
            System.out.println("Podany plik z danymi o infrastrukturze \"" + path + "\" nie istnieje.");
            return 1;
        } catch (NullPointerException ex) {
            System.out.println("Podany plik z danymi o infrastrukturze \"" + path + "\" jest pusty.");
            return 1;
        } catch (IOException ex) {
            System.out.println("Błąd odczytu pliku z danymi o infrastrukturze \"" + path + "\".");
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 1;
        }
        return 0;
    }
}