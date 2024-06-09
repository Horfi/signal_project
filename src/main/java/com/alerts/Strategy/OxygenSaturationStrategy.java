package com.alerts.Strategy;

import com.alerts.IAlert;
import com.alerts.factory.AlertFactory;
import com.alerts.factory.BloodOxygenAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {

    private AlertFactory alertFactory = new BloodOxygenAlertFactory();

    @Override
    public IAlert checkAlert(Patient patient) {
        if (patient == null) {
            System.err.println("ERROR: Patient data is null.");
            return null;
        }

        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        if (records == null || records.isEmpty()) {
            return null;
        }

        PatientRecord latestRecord = getLastRecord(records, "Saturation");
        if (latestRecord == null) {
            return null;
        }

        if (isLowSaturation(latestRecord.getMeasurementValue())) {
            return alertFactory.createAlert(String.valueOf(patient.getId()), "Low Blood Saturation", latestRecord.getTimestamp(), 0);
        }

        if (hasSignificantDrop(records, latestRecord)) {
            return alertFactory.createAlert(String.valueOf(patient.getId()), "Significant Saturation Drop", latestRecord.getTimestamp(), 1);
        }

        return null;
    }

    private PatientRecord getLastRecord(List<PatientRecord> records, String recordType) {
        return records.stream()
                .filter(record -> recordType.equals(record.getRecordType()))
                .max((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .orElse(null);
    }

    private boolean isLowSaturation(double value) {
        return value < 90;
    }

    private boolean hasSignificantDrop(List<PatientRecord> records, PatientRecord latestRecord) {
        double maxPreviousValue = records.stream()
                .filter(record -> "Saturation".equals(record.getRecordType()) && record.getTimestamp() < latestRecord.getTimestamp())
                .mapToDouble(PatientRecord::getMeasurementValue)
                .max()
                .orElse(Double.MIN_VALUE);

        return maxPreviousValue != Double.MIN_VALUE && maxPreviousValue - latestRecord.getMeasurementValue() > 5;
    }
}

