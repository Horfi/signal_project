package com.alerts.factory;

import com.alerts.IAlert;

public abstract class AlertFactory {
    public abstract IAlert createAlert(String patientId, String condition, long timestamp,int priority);

}
