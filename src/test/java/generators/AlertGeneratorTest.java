package generators;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setup() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
    }

    @Test
    void shouldTriggerHighHeartRateAlert() {
        dataStorage.addPatientData(10, 102.0, "HeartRate", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("High Heart Rate", alerts.get(0).getCondition());
    }

    @Test
    void shouldNotTriggerAlertForNormalHeartRate() {
        dataStorage.addPatientData(10, 75.0, "HeartRate", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertTrue(alerts.isEmpty());
    }

    @Test
    void shouldTriggerMultipleAlerts() {
        dataStorage.addPatientData(10, 104.0, "HeartRate", System.currentTimeMillis());
        dataStorage.addPatientData(10, 190.0, "Systolic", System.currentTimeMillis());
        dataStorage.addPatientData(10, 130.0, "Diastolic", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(3, alerts.size());
        assertEquals("High Heart Rate", alerts.get(0).getCondition());
        assertEquals("Critical Blood Pressure", alerts.get(1).getCondition());
        assertEquals("Critical Blood Pressure", alerts.get(2).getCondition());
    }

    @Test
    void shouldTriggerLowBloodPressureAlert() {
        dataStorage.addPatientData(10, 85.0, "Systolic", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Critical Blood Pressure", alerts.get(0).getCondition());
    }

    @Test
    void shouldTriggerLowOxygenSaturationAlert() {
        dataStorage.addPatientData(10, 88.0, "OxygenSaturation", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Low Blood Oxygen", alerts.get(0).getCondition());
    }

    @Test
    void shouldTriggerAbnormalECGAlert() {
        dataStorage.addPatientData(10, 1.8, "ECG", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Abnormal ECG", alerts.get(0).getCondition());
    }

    @Test
    void shouldTriggerHypotensiveHypoxemiaAlert() {
        dataStorage.addPatientData(10, 85.0, "Systolic", System.currentTimeMillis());
        dataStorage.addPatientData(10, 89.0, "OxygenSaturation", System.currentTimeMillis());

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(2, alerts.size());
        assertEquals("Hypotensive Hypoxemia", alerts.get(1).getCondition());
    }

    @Test
    void shouldTriggerIncreasingBloodPressureTrendAlert() {
        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(10, 150.0, "Systolic", currentTime - 30000);
        dataStorage.addPatientData(10, 160.0, "Systolic", currentTime - 20000);
        dataStorage.addPatientData(10, 170.0, "Systolic", currentTime - 10000);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Increasing Blood Pressure Trend", alerts.get(0).getCondition());
    }

    @Test
    void shouldTriggerDecreasingBloodPressureTrendAlert() {
        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(10, 170.0, "Systolic", currentTime - 30000);
        dataStorage.addPatientData(10, 160.0, "Systolic", currentTime - 20000);
        dataStorage.addPatientData(10, 150.0, "Systolic", currentTime - 10000);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Decreasing Blood Pressure Trend", alerts.get(0).getCondition());
    }

    @Test
    void shouldTriggerRapidBloodOxygenDropAlert() {
        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(10, 97.0, "OxygenSaturation", currentTime - 600000);
        dataStorage.addPatientData(10, 91.0, "OxygenSaturation", currentTime);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluatePatientData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertFalse(alerts.isEmpty());
        assertEquals("Rapid Oxygen Drop", alerts.get(0).getCondition());
    }
}
