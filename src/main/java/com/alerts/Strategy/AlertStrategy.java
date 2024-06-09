package com.alerts.Strategy;

import com.alerts.IAlert;
import com.data_management.Patient;

public interface AlertStrategy {
    IAlert checkAlert(Patient patient);
}
