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
    private final List<IAlert> alerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    public void evaluateData(Patient patient) {
        for (PatientRecord record : patient.getRecords(0, System.currentTimeMillis())) {
            AlertStrategy strategy = AlertStrategyFactory.getStrategy(record.getRecordType());
            if (strategy != null) {
                IAlert alert = strategy.checkAlert(patient);
                if (alert != null) {
                    alert = new PriorityAlertDecorator(alert, "high");
                    alert = new RepeatedAlertDecorator(alert, 3);
                    alerts.add(alert);
                }
            }
        }
    }

    public List<IAlert> getAlerts() {
        return alerts;
    }
}
