package com.alerts.Decorator;

import com.alerts.IAlert;

import java.util.List;

public class RepeatedAlertDecorator extends AlertDecorator {
    private final int repeatCount;

    public RepeatedAlertDecorator(IAlert alert, int repeatCount) {
        super(alert);
        this.repeatCount = repeatCount;
    }

    @Override
    public void alertAction(List<IAlert> alerts) {
        for (int i = 0; i < repeatCount; i++) {
            super.alertAction(alerts);
        }
    }
}
