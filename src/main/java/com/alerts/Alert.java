package com.alerts;

import java.util.List;

// Represents an alert
public class Alert implements IAlert {
    private String patientId;
    private String condition;
    private long timestamp;
    private int priority;

    public Alert(String patientId, String condition, long timestamp, int priority) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.priority = priority;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPriority() {
        return priority;
    }



}
