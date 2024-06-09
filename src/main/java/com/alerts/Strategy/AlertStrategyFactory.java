package com.alerts.Strategy;

import com.alerts.Alert;
import com.data_management.PatientRecord;

public class AlertStrategyFactory {
    public static AlertStrategy getStrategy(String recordType) {
        switch (recordType) {
            case "BloodPressure":
                return new BloodPressureStrategy();
            case "Hypoxia":
                return new HypotensiveHypoxemiaStrategy();
            case "HeartRate":
                return new HeartRateStrategy();
            case "Saturation":
                return new OxygenSaturationStrategy();
            default:
                return null;
        }
    }
}
