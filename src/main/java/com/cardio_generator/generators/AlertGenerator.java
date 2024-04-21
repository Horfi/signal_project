package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * Generates fake alerts for patients 
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();  // constant all upper case 
    private boolean[] AlertStates; // false = resolved, true = pressed
    /**
     * Set up our patients' alert states.
     * 
     * @param patientCount the number of patients we're keeping an eye on
     */
    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }
    // 
    @Override    /**
     * generate 
     * 
     * @param patientId the ID of the patient
     * @param outputStrategy how we're gonna blast this info out
     */
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve  0.9 should be a named constant rn its out of knowhere no description
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                //lambda should be uppercase cuz its a constant 
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency    
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
