package com.alerts.Decorator;

import java.util.List;

import com.alerts.IAlert;

public class PriorityAlertDecorator extends AlertDecorator {
    private final IAlert decoratedAlert;
    private final String priority;

    public PriorityAlertDecorator(IAlert decAlert, String priority) {
        this.decoratedAlert = decAlert;
        this.priority = priority;
    }

    @Override
    public String getPatientId() {
        return decoratedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Priority: " + priority + "]";
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }

}
