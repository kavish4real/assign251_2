package assign251_2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemAppenderTest {

    private MemAppender memAppender;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        String velocityPattern = "[$p] $c $d: $m";  // Example pattern
        VelocityLayout velocityLayout = new VelocityLayout(velocityPattern);  // Use VelocityLayout
        memAppender = MemAppender.getInstance("MemAppenderTest", velocityLayout, 10);
        logger = LogManager.getLogger(MemAppenderTest.class);
    }

    @Test
    public void testLogStorage() {
        // Simulate log events instead of adding the appender
        LogEvent event1 = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                .setMessage(new SimpleMessage("Test log 1")).build();
        LogEvent event2 = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                .setMessage(new SimpleMessage("Test log 2")).build();

        memAppender.append(event1);
        memAppender.append(event2);

        List<String> logs = memAppender.getEventStrings();
        assertEquals(2, logs.size(), "Log size should be 2");

        // Adjust this part to reflect the VelocityLayout pattern
        assertTrue(logs.get(0).contains("TestLogger"), "Pattern should contain the logger name");
        assertTrue(logs.get(1).contains("Test log 2"));

    }


    @Test
    public void testMaxSize() {
        // Simulate more log events than the maxSize
        for (int i = 0; i < 15; i++) {
            LogEvent event = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                    .setMessage(new SimpleMessage("Log " + i)).build();
            memAppender.append(event);
        }

        List<String> logs = memAppender.getEventStrings();
        assertEquals(10, logs.size(), "Log size should not exceed maxSize");
        assertTrue(logs.get(0).contains("Log 5"), "Oldest logs should be discarded");
    }

    @Test
    public void testDiscardedLogCount() {
        // Simulate more log events than the maxSize to force discarding
        for (int i = 0; i < 15; i++) {
            LogEvent event = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                    .setMessage(new SimpleMessage("Discard Log " + i)).build();
            memAppender.append(event);

            // Debugging print to check discarded log count
            System.out.println("Discarded log count after " + i + " logs: " + memAppender.getDiscardedLogCount());
        }

        // Assert that 7 logs are discarded when 15 logs are appended and the maxSize is 10
        assertEquals(7, memAppender.getDiscardedLogCount(), "7 logs should be discarded");
    }
}
