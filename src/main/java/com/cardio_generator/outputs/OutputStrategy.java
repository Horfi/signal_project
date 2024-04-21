package com.cardio_generator.outputs;

/**
 * Use this to spit out patient data 
 */
public interface OutputStrategy {
    
    /**
     * Take this data and diplay it
     * 
     * @param patientId Unique ID for the patient.
     * @param timestamp When it all went down.
     * @param label Kind of data - like 'heartbeat'.
     * @param data The actual info bits.
     */
    void output(int patientId, long timestamp, String label, String data);
}
