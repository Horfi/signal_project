package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.alerts.IAlert;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class AlertGeneratorTest {
    private DataStorage storage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        storage = new DataStorage();
        alertGenerator = new AlertGenerator(storage);
    }

    @Test
    void testNoAlerts() {
        Patient patient = createNormalPatient();
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testHighHeartRateAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(101.0, "HeartRate", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<IAlert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("Abnormal Heart Rate [Priority: High]", alerts.get(0).getCondition());
    }

    @Test
    void testLowBloodSaturationAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(89.0, "OxygenSaturation", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<IAlert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size());
        assertEquals("Low Blood Saturation [Priority: High]", alerts.get(0).getCondition());
    }

    @Test
    void testMultipleAlerts() {
        Patient patient = new Patient(1);
        patient.addRecord(101.0, "HeartRate", System.currentTimeMillis());
        patient.addRecord(89.0, "OxygenSaturation", System.currentTimeMillis());
        patient.addRecord(150.0, "BloodPressure", System.currentTimeMillis());
        patient.addRecord(70.0, "BloodPressure", System.currentTimeMillis());

        alertGenerator.evaluateData(patient);

        List<IAlert> alerts = alertGenerator.getAlerts();
        assertEquals(4, alerts.size());

        assertEquals("Abnormal Heart Rate [Priority: High]", alerts.get(0).getCondition());
        assertEquals("Low Blood Saturation [Priority: High]", alerts.get(1).getCondition());
        assertEquals("Critical Blood Pressure [Priority: High]", alerts.get(2).getCondition());
        assertEquals("Critical Blood Pressure [Priority: High]", alerts.get(3).getCondition());
    }

    private Patient createNormalPatient() {
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789010L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789052L);
        patient.addRecord(98.0, "Saturation", 1714376789053L);
        patient.addRecord(60.0, "ECG", 1714376789054L);
        return patient;
    }
}
