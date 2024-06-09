package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageTest {

    private DataStorage dataStorage;

    @BeforeEach
    void setUp() {
        dataStorage = new DataStorage();
    }

    @Test
    void addPatientData_storesDataCorrectly() {
        int patientId = 1;
        double measurementValue = 120.5;
        String recordType = "HeartRate";
        long timestamp = System.currentTimeMillis();

        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);

        List<PatientRecord> records = dataStorage.getRecords(patientId, timestamp - 1000, timestamp + 1000);
        assertNotNull(records);
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(patientId, record.getPatientId());
        assertEquals(measurementValue, record.getMeasurementValue());
        assertEquals(recordType, record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());
    }

    @Test
    void getRecords_filtersRecordsByTime() {
        int patientId = 1;
        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(patientId, 120.0, "HeartRate", currentTime);
        dataStorage.addPatientData(patientId, 130.0, "HeartRate", currentTime + 60000);

        List<PatientRecord> records = dataStorage.getRecords(patientId, currentTime, currentTime + 30000);
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());

        records = dataStorage.getRecords(patientId, currentTime, currentTime + 70000);
        assertEquals(2, records.size());
    }

    @Test
    void getAllPatients_returnsAllPatients() {
        dataStorage.addPatientData(1, 120.0, "HeartRate", System.currentTimeMillis());
        dataStorage.addPatientData(2, 130.0, "HeartRate", System.currentTimeMillis() + 60000);

        List<Patient> patients = dataStorage.getAllPatients();
        assertNotNull(patients);
        assertEquals(2, patients.size());
    }
}
