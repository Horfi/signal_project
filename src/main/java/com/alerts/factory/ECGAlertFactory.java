package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.IAlert;

public class ECGAlertFactory extends AlertFactory{


    @Override
    public IAlert createAlert(String patientId, String condition, long timestamp,int priority) {
        IAlert alert = new Alert(patientId, condition, timestamp, priority);
        return alert;
    }
}

