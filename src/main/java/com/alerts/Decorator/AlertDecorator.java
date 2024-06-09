package com.alerts.Decorator;

import com.alerts.IAlert;

public abstract class AlertDecorator implements IAlert {
    protected IAlert decAlert;

    public AlertDecorator(IAlert decoratedAlert) {
        this.decAlert = decoratedAlert;
    }

    @Override
    public String getPatientId() {
        return decAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return decAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return decAlert.getTimestamp();
    }
    @Override
    public int getPriority() {
        return decAlert.getPriority();
    }
}