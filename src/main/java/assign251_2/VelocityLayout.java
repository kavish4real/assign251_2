package assign251_2;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;

public class VelocityLayout extends AbstractStringLayout {

    private final VelocityEngine velocityEngine;
    private final String pattern;

    public VelocityLayout(String pattern) {
        super(Charset.forName("UTF-8"));
        this.pattern = pattern;

        // Initialize VelocityEngine
        velocityEngine = new VelocityEngine();
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init(properties);
    }

    @Override
    public String toSerializable(LogEvent event) {
        VelocityContext context = new VelocityContext();
        context.put("c", event.getLoggerName());
        context.put("d", event.getTimeMillis());
        context.put("m", event.getMessage().getFormattedMessage());
        context.put("p", event.getLevel().toString());
        context.put("t", Thread.currentThread().getName());
        context.put("n", System.lineSeparator());

        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(context, writer, "VelocityLayout", pattern);

        return writer.toString();
    }

    public String getContentType() {
        return "text/plain";
    }
}
