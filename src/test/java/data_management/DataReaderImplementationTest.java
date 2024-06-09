package data_management;

import com.data_management.DataReaderImplementation;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataReaderImplementationTest {

    //temporrary directionary that has files 
    Path tempDir;



    @Test
    void testReadData() throws IOException {
        File tempFile = tempDir.resolve("data.txt").toFile(); //temp file 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            //some random test data 
            writer.write("1, 100.0, 90, 1714376789050\n");
            writer.write("2, 120.0, 70, 1714376789051\n");
        }
        ///storage 
        DataStorage dataStorage = new DataStorage();
        new DataReaderImplementation(tempDir.toFile()).readData(dataStorage);

        //get records
        List<PatientRecord> records = dataStorage.getRecords();
        assertEquals(2, records.size());

        //validate 1st and 2nd records of data
        PatientRecord firstRecord = records.get(0);
        assertEquals(1, firstRecord.getPatientId());
        assertEquals(100.0, firstRecord.getMeasurementValue());

        PatientRecord secondRecord = records.get(1);
        assertEquals(2, secondRecord.getPatientId());
        assertEquals(120.0, secondRecord.getMeasurementValue());
    }
}
