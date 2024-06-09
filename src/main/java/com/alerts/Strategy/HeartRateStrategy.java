package com.alerts.Strategy;

import com.alerts.Alert;
import com.alerts.IAlert;
import com.alerts.factory.AlertFactory;
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

        PatientRecord lastUploaded = records.get(records.size() - 1);
        double currentValue = lastUploaded.getMeasurementValue();

        if (currentValue > 100) {
            IAlert alert = new Alert(String.valueOf(patient.getId()), "Abnormal Heart Rate", lastUploaded.getTimestamp(), 1);
            return alert;
        }

        return null;
    }
}
