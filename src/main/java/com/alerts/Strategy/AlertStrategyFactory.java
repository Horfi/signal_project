package com.alerts.Strategy;

import com.alerts.Strategy.BloodPressureStrategy;
import com.alerts.Strategy.HeartRateStrategy;
import com.alerts.Strategy.OxygenSaturationStrategy;

public class AlertStrategyFactory {
    public static AlertStrategy getStrategy(String recordType) {
        switch (recordType) {
            case "BloodPressure":
                return new BloodPressureStrategy();
            case "HeartRate":
                return new HeartRateStrategy();
            case "OxygenSaturation":
                return new OxygenSaturationStrategy();
            default:
                return null;
        }
    }
}
