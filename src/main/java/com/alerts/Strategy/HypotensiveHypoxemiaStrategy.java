package com.alerts.Strategy;

import com.alerts.IAlert;
import com.alerts.factory.AlertFactory;
import com.alerts.factory.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HypotensiveHypoxemiaStrategy implements AlertStrategy {

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

        PatientRecord systolicPressureRecord = findLatestRecord(records, "SystolicPressure");
        PatientRecord bloodSaturationRecord = findLatestRecord(records, "Saturation");

        if (systolicPressureRecord != null && bloodSaturationRecord != null) {
            if (isHypoxia(systolicPressureRecord.getMeasurementValue(), bloodSaturationRecord.getMeasurementValue())) {
                AlertFactory factory = new HypotensiveHypoxemiaAlertFactory();
                return factory.createAlert(String.valueOf(patient.getId()), "Hypoxia", Math.max(systolicPressureRecord.getTimestamp(), bloodSaturationRecord.getTimestamp()),1);
            }
        }

        return null;
    }

    private PatientRecord findLatestRecord(List<PatientRecord> records, String recordType) {
        return records.stream()
                .filter(record -> recordType.equals(record.getRecordType()))
                .max((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()))
                .orElse(null);
    }

    private boolean isHypoxia(double systolicPressure, double bloodSaturation) {
        return systolicPressure < 90 && bloodSaturation <= 92;
    }
}
