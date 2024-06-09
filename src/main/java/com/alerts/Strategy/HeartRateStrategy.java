package com.alerts.Strategy;

import com.alerts.Alert;
import com.alerts.IAlert;
import com.alerts.factory.ECGAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public IAlert checkAlert(Patient patient) {
        if (patient == null) {
            System.out.println("ERROR: Patient data is null.");
            return null;
        }

        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        if (records == null || records.isEmpty()) {
            return null;
        }

        for (PatientRecord record : records) {
            if ("HeartRate".equals(record.getRecordType()) && (record.getMeasurementValue() > 100 || record.getMeasurementValue() < 60)) {
                return new ECGAlertFactory().createAlert(String.valueOf(patient.getId()), "Abnormal Heart Rate", record.getTimestamp(),1);
            }
        }

        return null;
    }
}
