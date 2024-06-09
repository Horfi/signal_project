package com.alerts.decorators;

import com.alerts.IAlert;

import java.util.List;

public class RepeatedAlertDecorator implements IAlert {
    private final IAlert decoratedAlert;
    private final int repeatCount;

    public RepeatedAlertDecorator(IAlert alert, int repeatCount) {
        this.decoratedAlert = alert;
        this.repeatCount = repeatCount;
    }

    @Override
    public void alertAction(List<IAlert> alerts) {
        for (int i = 0; i < repeatCount; i++) {
            decoratedAlert.alertAction(alerts);
        }
    }

    @Override
    public String getPatientId() {
        return decoratedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Repeated]";
    }

    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }

    @Override
    public int getSeverity() {
        return decoratedAlert.getSeverity();
    }
}
