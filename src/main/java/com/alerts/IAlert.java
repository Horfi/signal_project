package com.alerts;

import java.util.List;

public interface IAlert {
    void alertAction(List<IAlert> alerts);
    String getPatientId();
    String getCondition();
    long getTimestamp();
    int getPriority();
} 