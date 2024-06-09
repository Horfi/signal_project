package com.alerts.Decorator;

import com.alerts.IAlert;
import java.util.List;

public class PriorityAlertDecorator extends AlertDecorator {
    private final String priority;

    public PriorityAlertDecorator(IAlert alert, String priority) {
        super(alert);
        this.priority = priority;
    }

    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " [Priority: " + priority + "]";
    }

    @Override
    public void alertAction(List<IAlert> alerts) {
        super.alertAction(alerts);
    }

    @Override
    public int getPriority() {
        return decoratedAlert.getPriority();
    }
}
