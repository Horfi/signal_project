package com.alerts;

import java.util.List;

public interface IAlert {
    String getPatientId();
    String getCondition();
    long getTimestamp();
    int getPriority();
    
} 