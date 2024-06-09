package data_management;

import com.data_management.DataReaderImplementation;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataReaderImplementationTest {

    @TempDir
    Path tempDir;  // Temporary directory for storing test files

    @Test
    void testReadData() throws IOException {
        // Create temporary files in the temporary directory
        File tempFile1 = tempDir.resolve("patient_data1.txt").toFile();
        File tempFile2 = tempDir.resolve("patient_data2.txt").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile1))) {
            writer.write("11, 100.0, HeartRate, 1714376789050\n");
            writer.write("12, 120.0, HeartRate, 1714376789051\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile2))) {
            writer.write("13, 80.0, BloodPressure, 1714376789052\n");
            writer.write("14, 140.0, BloodPressure, 1714376789053\n");
        }

        DataStorage dataStorage = new DataStorage();
        DataReaderImplementation reader = new DataReaderImplementation(tempDir.toFile());
        reader.readData(dataStorage);

        List<Patient> patients = dataStorage.getAllPatients();
        assertEquals(4, patients.size());

        // Validate records for patient 11
        Patient patient11 = patients.stream().filter(p -> p.getId() == 11).findFirst().orElse(null);
        assertNotNull(patient11);
        List<PatientRecord> recordsPatient11 = patient11.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, recordsPatient11.size());
        PatientRecord record11 = recordsPatient11.get(0);
        assertEquals(11, record11.getPatientId());
        assertEquals(100.0, record11.getMeasurementValue());

        // Validate records for patient 12
        Patient patient12 = patients.stream().filter(p -> p.getId() == 12).findFirst().orElse(null);
        assertNotNull(patient12);
        List<PatientRecord> recordsPatient12 = patient12.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, recordsPatient12.size());
        PatientRecord record12 = recordsPatient12.get(0);
        assertEquals(12, record12.getPatientId());
        assertEquals(120.0, record12.getMeasurementValue());

        // Validate records for patient 13
        Patient patient13 = patients.stream().filter(p -> p.getId() == 13).findFirst().orElse(null);
        assertNotNull(patient13);
        List<PatientRecord> recordsPatient13 = patient13.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, recordsPatient13.size());
        PatientRecord record13 = recordsPatient13.get(0);
        assertEquals(13, record13.getPatientId());
        assertEquals(80.0, record13.getMeasurementValue());

        // Validate records for patient 14
        Patient patient14 = patients.stream().filter(p -> p.getId() == 14).findFirst().orElse(null);
        assertNotNull(patient14);
        List<PatientRecord> recordsPatient14 = patient14.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, recordsPatient14.size());
        PatientRecord record14 = recordsPatient14.get(0);
        assertEquals(14, record14.getPatientId());
        assertEquals(140.0, record14.getMeasurementValue());
    }
}
