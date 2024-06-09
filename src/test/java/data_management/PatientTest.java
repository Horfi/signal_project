package data_management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(1);
    }

    @Test
    void addRecord_addsRecordCorrectly() {
        double measurementValue = 120.5;
        String recordType = "HeartRate";
        long timestamp = System.currentTimeMillis();

        patient.addRecord(measurementValue, recordType, timestamp);

        List<PatientRecord> records = patient.getRecords(timestamp - 1000, timestamp + 1000);
        assertNotNull(records);
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(measurementValue, record.getMeasurementValue());
        assertEquals(recordType, record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());
    }

    @Test
    void getRecords_filtersRecordsByTime() {
        long currentTime = System.currentTimeMillis();
        patient.addRecord(120.0, "HeartRate", currentTime);
        patient.addRecord(130.0, "HeartRate", currentTime + 60000);

        List<PatientRecord> records = patient.getRecords(currentTime, currentTime + 30000);
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());

        records = patient.getRecords(currentTime, currentTime + 70000);
        assertEquals(2, records.size());
    }
}
