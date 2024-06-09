package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.Strategy.AlertStrategy;
import com.alerts.Strategy.AlertStrategyFactory;
import com.alerts.Decorator.PriorityAlertDecorator;
import com.alerts.Decorator.RepeatedAlertDecorator;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private final DataStorage dataStorage;
    private final List<IAlert> generatedAlerts;
    private final List<AlertStrategy> alertStrategies;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.generatedAlerts = new ArrayList<>();
        this.alertStrategies = initializeStrategies();
    }

    private List<AlertStrategy> initializeStrategies() {
        List<AlertStrategy> strategies = new ArrayList<>();
        strategies.add(AlertStrategyFactory.getStrategy("BloodPressure"));
        strategies.add(AlertStrategyFactory.getStrategy("Hypoxia"));
        strategies.add(AlertStrategyFactory.getStrategy("HeartRate"));
        strategies.add(AlertStrategyFactory.getStrategy("Saturation"));
        return strategies;
    }

    public void analyzePatientData(Patient patient) {
        if (patient == null) {
            System.err.println("ERROR: Patient data is null.");
            return;
        }

        List<PatientRecord> records = patient.getRecords(0, System.currentTimeMillis());
        for (PatientRecord record : records) {
            AlertStrategy strategy = getStrategyForRecord(record);
            if (strategy != null) {
                IAlert alert = strategy.checkAlert(record);
                if (alert != null) {
                    alert = new PriorityAlertDecorator(alert, "High");
                    alert = new RepeatedAlertDecorator(alert, 3);
                    generatedAlerts.add(alert);
                }
            }
        }
    }

    private AlertStrategy getStrategyForRecord(PatientRecord record) {
        for (AlertStrategy strategy : alertStrategies) {
            if (strategy.isApplicable(record)) {
                return strategy;
            }
        }
        return null;
    }

    public List<IAlert> getGeneratedAlerts() {
        return generatedAlerts;
    }
}
