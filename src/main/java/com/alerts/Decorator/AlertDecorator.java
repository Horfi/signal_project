package com.alerts.Decorator;

import com.alerts.IAlert;

import java.util.List;

public abstract class AlertDecorator implements IAlert {
    protected final IAlert decoratedAlert;

    public AlertDecorator(IAlert alert) {
        this.decoratedAlert = alert;
    }

    @Override
    public String getPatientId() {
        return decoratedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }

    @Override
    public int getPriority() {
        return decoratedAlert.getPriority();
    }

    @Override
    public void alertAction(List<IAlert> alerts) {
        decoratedAlert.alertAction(alerts);
    }
}
