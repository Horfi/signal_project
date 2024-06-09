package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DataReaderImplementation implements DataReader {
    private final File directory;

    //checks if provided path is valid if not throws exception
    public DataReaderImplementation(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Specified path is not a directory: " + directory);
        }
        this.directory = directory;
    }

    //iterates through files and calls parseAndStoreData for each one 
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    parseAndStoreData(file, dataStorage);
                }
            }
        }
    }

    // opens each file reads the data, each data is added to storage
    private void parseAndStoreData(File file, DataStorage dataStorage) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    try {
                        int patientId = Integer.parseInt(parts[0].trim());
                        double measurementValue = Double.parseDouble(parts[1].trim());
                        String recordType = parts[2].trim();
                        long timestamp = Long.parseLong(parts[3].trim());

                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid data format in file: " + file.getName() + " line: " + line);
                    }
                } else {
                    System.err.println("Ignored malformed line in file: " + file.getName() + " line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getName());
            e.printStackTrace();
        }
    }
}
