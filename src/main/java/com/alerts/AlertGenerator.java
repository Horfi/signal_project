package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    public void evaluatePatientData(Patient patient) {
        long startTime = 0;
        long endTime = System.currentTimeMillis();
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        for (PatientRecord record : records) {
            evaluateBloodPressureTrend(patient, record);
            evaluateCriticalBloodPressure(patient, record);
            checkHeartRate(patient, record);
            checkBloodOxygen(patient, record);
            checkECGData(patient, record);
        }

        checkCombinedCondition(patient, records);
    }

    private void checkHeartRate(Patient patient, PatientRecord record) {
        if ("HeartRate".equals(record.getRecordType()) && record.getMeasurementValue() > 100) {
            createAlert(patient.getId(), "High Heart Rate", record.getTimestamp(), "Heart rate exceeds 100 bpm.");
        }
    }

    private void evaluateCriticalBloodPressure(Patient patient, PatientRecord record) {
        double systolic = 0;
        double diastolic = 0;
        if ("Systolic".equals(record.getRecordType())) {
            systolic = record.getMeasurementValue();
        } else if ("Diastolic".equals(record.getRecordType())) {
            diastolic = record.getMeasurementValue();
        }

        if (systolic > 180 || systolic < 90 || diastolic > 120 || diastolic < 60) {
            createAlert(patient.getId(), "Critical Blood Pressure", record.getTimestamp(), "Critical blood pressure levels detected.");
        }
    }

    private void evaluateBloodPressureTrend(Patient patient, PatientRecord record) {
        List<PatientRecord> systolicRecords = getRecentRecords(patient, "Systolic", 3);
        List<PatientRecord> diastolicRecords = getRecentRecords(patient, "Diastolic", 3);

        if (systolicRecords.size() < 3 || diastolicRecords.size() < 3) return;

        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < systolicRecords.size(); i++) {
            double prevSystolic = systolicRecords.get(i - 1).getMeasurementValue();
            double currSystolic = systolicRecords.get(i).getMeasurementValue();
            double prevDiastolic = diastolicRecords.get(i - 1).getMeasurementValue();
            double currDiastolic = diastolicRecords.get(i).getMeasurementValue();

            if (currSystolic - prevSystolic <= 10 || currDiastolic - prevDiastolic <= 10) isIncreasing = false;
            if (prevSystolic - currSystolic <= 10 || prevDiastolic - currDiastolic <= 10) isDecreasing = false;
        }

        if (isIncreasing) {
            createAlert(patient.getId(), "Increasing Blood Pressure Trend", record.getTimestamp(), "Blood pressure trend increasing over 3 readings.");
        } else if (isDecreasing) {
            createAlert(patient.getId(), "Decreasing Blood Pressure Trend", record.getTimestamp(), "Blood pressure trend decreasing over 3 readings.");
        }
    }

    private void checkBloodOxygen(Patient patient, PatientRecord record) {
        if ("OxygenSaturation".equals(record.getRecordType())) {
            double value = record.getMeasurementValue();
            if (value < 92) {
                createAlert(patient.getId(), "Low Blood Oxygen", record.getTimestamp(), "Blood oxygen below 92%.");
            }
            evaluateRapidOxygenDrop(patient, record);
        }
    }

    private void evaluateRapidOxygenDrop(Patient patient, PatientRecord record) {
        List<PatientRecord> oxygenRecords = getRecordsInInterval(patient, "OxygenSaturation", record.getTimestamp() - 600000, record.getTimestamp());

        if (oxygenRecords.size() < 2) return;

        double start = oxygenRecords.get(0).getMeasurementValue();
        double end = oxygenRecords.get(oxygenRecords.size() - 1).getMeasurementValue();

        if (start - end >= 5) {
            createAlert(patient.getId(), "Rapid Oxygen Drop", record.getTimestamp(), "Blood oxygen dropped by 5% or more within 10 minutes.");
        }
    }

    private void checkECGData(Patient patient, PatientRecord record) {
        if ("ECG".equals(record.getRecordType()) && record.getMeasurementValue() > 1.5) {
            createAlert(patient.getId(), "Abnormal ECG", record.getTimestamp(), "ECG value exceeds 1.5 mV.");
        }
    }

    private void checkCombinedCondition(Patient patient, List<PatientRecord> records) {
        boolean lowBP = false;
        boolean lowOxygen = false;

        for (PatientRecord record : records) {
            if ("Systolic".equals(record.getRecordType()) && record.getMeasurementValue() < 90) {
                lowBP = true;
            }
            if ("OxygenSaturation".equals(record.getRecordType()) && record.getMeasurementValue() < 92) {
                lowOxygen = true;
            }
        }

        if (lowBP && lowOxygen) {
            createAlert(patient.getId(), "Hypotensive Hypoxemia", System.currentTimeMillis(), "Low blood pressure and low blood oxygen detected together.");
        }
    }

    private void createAlert(int patientId, String condition, long timestamp, String message) {
        Alert alert = new Alert(String.valueOf(patientId), condition, timestamp);
        alerts.add(alert);
        System.out.println("Alert: " + message);
    }

    public List<Alert> getAlerts() {
        return new ArrayList<>(alerts);
    }

    private List<PatientRecord> getRecentRecords(Patient patient, String recordType, int count) {
        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        List<PatientRecord> filtered = new ArrayList<>();
        for (PatientRecord record : records) {
            if (recordType.equals(record.getRecordType())) {
                filtered.add(record);
            }
        }
        return filtered.subList(Math.max(filtered.size() - count, 0), filtered.size());
    }

    private List<PatientRecord> getRecordsInInterval(Patient patient, String recordType, long start, long end) {
        List<PatientRecord> records = patient.getRecords(start, end);
        List<PatientRecord> filtered = new ArrayList<>();
        for (PatientRecord record : records) {
            if (recordType.equals(record.getRecordType())) {
                filtered.add(record);
            }
        }
        return filtered;
    }
}
