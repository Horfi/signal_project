package output;

import com.cardio_generator.outputs.TcpOutputStrategy;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TcpOutputStrategyTest {

    private TcpOutputStrategy tcpOutputStrategy;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        tcpOutputStrategy = new TcpOutputStrategy(8080);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void output_sendsDataCorrectly() {
        PatientRecord testData = new PatientRecord(1, System.currentTimeMillis(), "HeartRate", 120);
        tcpOutputStrategy.output(1, System.currentTimeMillis(), "HeartRate", "120");

        String expectedOutput = "Data sent to TCP server: " + testData.toString() + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
