package com.alerts.Strategy;

import com.alerts.IAlert;
import com.alerts.factory.AlertFactory;
import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

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

        AlertFactory factory = new BloodPressureAlertFactory();
        if (isCriticalThresholdAlert(lastUploaded, currentValue)) {
            return factory.createAlert(String.valueOf(patient.getId()), "Critical Threshold Alert pressure", lastUploaded.getTimestamp(), 1);
        }

        if (trendAlert(records, lastUploaded)) {
            return factory.createAlert(String.valueOf(patient.getId()), "Trend Alert", lastUploaded.getTimestamp(), 1);
        }

        return null;
    }

    private boolean isCriticalThresholdAlert(PatientRecord record, double value) {
        return (record.getRecordType().equals("SystolicPressure") && (value > 180 || value < 90)) ||
               (record.getRecordType().equals("DiastolicPressure") && (value > 120 || value < 60));
    }

    private boolean trendAlert(List<PatientRecord> records, PatientRecord lastRecord) {
        if (records.size() < 3) return false;

        List<Double> values = new ArrayList<>();
        values.add(lastRecord.getMeasurementValue());

        for (int i = records.size() - 2; i >= 0; i--) {
            if (values.size() >= 3) break;
            if (records.get(i).getRecordType().equals(lastRecord.getRecordType())) {
                values.add(records.get(i).getMeasurementValue());
            }
        }

        if (values.size() < 3) return false;

        return (values.get(0) - values.get(1) > 10 && values.get(1) - values.get(2) > 10) ||
               (values.get(0) - values.get(1) < -10 && values.get(1) - values.get(2) < -10);
    }
}
