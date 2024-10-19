package assign251_2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppenderLayoutTest {

    private MemAppender memAppender;
    private Logger logger;

    @BeforeEach
    public void setUp() {
        // Use the VelocityLayout for testing
        String velocityPattern = "[$p] $c $d: $m";
        VelocityLayout velocityLayout = new VelocityLayout(velocityPattern);
        memAppender = MemAppender.getInstance("TestAppender", velocityLayout, 10);
        logger = LogManager.getLogger("TestLogger");
    }

    @Test
    public void testLogWithInfoLevel() {
        // Simulate log events at different levels
        LogEvent event = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                .setMessage(new SimpleMessage("Info log message"))
                .setLevel(org.apache.logging.log4j.Level.INFO).build();

        memAppender.append(event);

        List<String> logs = memAppender.getEventStrings();
        assertEquals(1, logs.size(), "Should only have one log event.");
        assertTrue(logs.get(0).contains("[INFO]"), "Log should contain INFO level.");
    }

    @Test
    public void testLogWithDebugLevel() {
        LogEvent event = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                .setMessage(new SimpleMessage("Debug log message"))
                .setLevel(org.apache.logging.log4j.Level.DEBUG).build();

        memAppender.append(event);

        List<String> logs = memAppender.getEventStrings();
        assertEquals(1, logs.size(), "Should only have one log event.");
        assertTrue(logs.get(0).contains("[DEBUG]"), "Log should contain DEBUG level.");
    }

    @Test
    public void testCombinationOfLayouts() {
        // Switch to using PatternLayout instead of VelocityLayout
        org.apache.logging.log4j.core.layout.PatternLayout patternLayout = org.apache.logging.log4j.core.layout.PatternLayout.createDefaultLayout();
        MemAppender patternAppender = MemAppender.getInstance("PatternAppender", patternLayout, 10);

        LogEvent event = Log4jLogEvent.newBuilder().setLoggerName("TestLogger")
                .setMessage(new SimpleMessage("Pattern layout log"))
                .setLevel(org.apache.logging.log4j.Level.INFO).build();

        patternAppender.append(event);

        List<String> logs = patternAppender.getEventStrings();
        assertEquals(1, logs.size(), "Should have one log event with PatternLayout.");
        assertTrue(logs.get(0).contains("INFO"), "Pattern layout should include log level INFO.");
    }
}
