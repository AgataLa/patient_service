package PatientService.file_utils;

import PatientService.models.Patient;

import java.io.*;
import java.util.Map;

public class FileServicePatient {

    public int readData(String path, Map<Integer, Patient> patients) {

        try {
            if (!path.contains(".txt")) {
                throw new Exception("Podany plik \"" + path + "\" ma niepoprawny format. Plik z danymi o pacjentach musi być plikiem tekstowym z rozszerzeniem \".txt\".");
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

            if (line.contains("# Pacjenci")) {
                while ((line = br.readLine()) != null) {
                    lineNr++;
                    if (dv.isEmpty(line, lineNr)) {
                        continue;
                    }
                    if (dv.checkPatient(lineNr, line, patients) == 1)
                        return 1;
                }
                System.out.println("Udało się wczytać dane z pliku o pacjentach. Pacjenci w kolejce do obsługi.");
            } else {
                throw new Exception("Brak/niepoprawny format nagłówka dla pacjentów.");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Podany plik z danymi o pacjentach \"" + path + "\" nie istnieje.");
            return 1;
        } catch (NullPointerException ex) {
            System.out.println("Podany plik z danymi o pacjentach \"" + path + "\" jest pusty.");
            return 1;
        } catch (IOException ex) {
            System.out.println("Błąd odczytu pliku z danymi o pacjentach \"" + path + "\".");
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 1;
        }
        return 0;
    }
}