package org.foo.shell.commands;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class FrameworkPropertiesCommand extends BasicCommand {
    private static final List<String> PROPERTY_NAMES = Arrays.asList(
            "org.osgi.framework.version",
            "org.osgi.framework.vendor",
            "org.osgi.framework.language",
            "org.osgi.framework.os.name",
            "org.osgi.framework.os.version",
            "org.osgi.framework.processor"
    );

    private static final int PROPERTY_NAMES_MAX_LENGTH = PROPERTY_NAMES.stream()
            .mapToInt(String::length)
            .max().getAsInt();

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        String format = "%-" + PROPERTY_NAMES_MAX_LENGTH + "s : %s%n";
        for (String propertyName : PROPERTY_NAMES) {
            out.printf(format, propertyName, getContext().getProperty(propertyName));
        }
    }
}
