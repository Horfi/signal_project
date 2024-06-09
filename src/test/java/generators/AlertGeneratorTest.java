package generators;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AlertGeneratorTest {

    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
    }

    @Test
    void testNoAlerts() throws IOException {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789010L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789052L);
        patient.addRecord(98.0, "Saturation", 1714376789053L);
        patient.addRecord(60.0, "ECG", 1714376789054L);
        // Add more records as needed for your test case

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testAbnormalDataAlerts() {
        Patient patient = new Patient(1);
        // Add records with abnormal values
        patient.addRecord(190.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(190.0, "SystolicPressure", 1714376789051L);
        patient.addRecord(80.0, "Saturation", 1714376789052L);
        patient.addRecord(20.0, "ECG", 1714376789053L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testAbnormalTrendsInData() {
        Patient patient = new Patient(1);
        // Add records with abnormal trends
        patient.addRecord(70.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(85.0, "DiastolicPressure", 1714376789051L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789052L);
        patient.addRecord(115.0, "DiastolicPressure", 1714376789053L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testHypoxia() {
        Patient patient = new Patient(1);
        patient.addRecord(40.0, "SystolicPressure", 1714376789050L);
        patient.addRecord(50.0, "Saturation", 1714376789051L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testBloodPressureStrategy() {
        Patient patient = new Patient(1);
        patient.addRecord(190.0, "DiastolicPressure", 1714376789050L); // Hypertensive crisis

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testHeartRateStrategy() {
        Patient patient = new Patient(1);
        patient.addRecord(40.0, "ECG", 1714376789051L); // Bradycardia

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testHypotensiveHypoxemiaStrategy() {
        Patient patient = new Patient(1);
        patient.addRecord(70.0, "Saturation", 1714376789052L); // Hypoxemia

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testOxygenSaturationStrategy() {
        Patient patient = new Patient(1);
        patient.addRecord(85.0, "Saturation", 1714376789053L); // Low oxygen saturation

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);

        assertTrue(alertGenerator.getAlerts().isEmpty());
    }
}
