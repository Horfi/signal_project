package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
/**
 * Defines how to cook up some fake patient data.
 * Anything that wants to pretend to be a data gen needs to follow this blueprint.
 */
public interface PatientDataGenerator {
    /**
         * @param patientId The ID of the patient – gotta know who we're talking about.
     * @param outputStrategy The method we're gonna use to get this data out there.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
