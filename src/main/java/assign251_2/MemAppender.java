package assign251_2;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Plugin(name = "MemAppender", category = "Core", elementType = Appender.ELEMENT_TYPE, printObject = true)
public class MemAppender extends AbstractAppender {

    private static MemAppender instance;
    private final List<LogEvent> logEvents;
    private final int maxSize;
    private long discardedLogCount = 0;

    // Private constructor for Singleton
    private MemAppender(String name, Filter filter, Layout<? extends Serializable> layout, int maxSize) {
        super(name, filter, layout, true);
        this.logEvents = new ArrayList<>();
        this.maxSize = maxSize;
    }

    // Singleton pattern
    public static MemAppender getInstance(String name, Layout<? extends Serializable> layout, int maxSize) {
        if (instance == null) {
            instance = new MemAppender(name, null, layout != null ? layout : PatternLayout.createDefaultLayout(), maxSize);
        }
        return instance;
    }

    @Override
    public void append(LogEvent event) {
        // Add the new event
        logEvents.add(event);
        System.out.println("Log added: " + event.getMessage().getFormattedMessage());

        // Now check if the list size exceeds the maxSize after adding the new log
        while (logEvents.size() > maxSize) {
            System.out.println("Discarding log: " + logEvents.get(0).getMessage().getFormattedMessage());
            logEvents.remove(0);  // Remove the oldest log
            discardedLogCount++;
        }

        System.out.println("Current discarded log count: " + discardedLogCount);
    }

    public List<LogEvent> getCurrentLogs() {
        return Collections.unmodifiableList(logEvents);
    }

    public List<String> getEventStrings() {
        List<String> eventStrings = new ArrayList<>();
        for (LogEvent event : logEvents) {
            eventStrings.add(new String(getLayout().toByteArray(event)));
        }
        return Collections.unmodifiableList(eventStrings);
    }

    public void printLogs() {
        for (LogEvent event : logEvents) {
            System.out.print(new String(getLayout().toByteArray(event)));
        }
        logEvents.clear();
    }

    public long getDiscardedLogCount() {
        return discardedLogCount;
    }
}
