package org.foo.shell;

import org.foo.shell.commands.HelpCommand;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Activator implements BundleActivator {
    private final AtomicReference<Binding> bindingRef = new AtomicReference<>();

    @SuppressWarnings("java:S106")  // Bundle будет запускаться в чистой среде, в которой не будет логгера
    @Override
    public void start(BundleContext context) throws Exception {
        int port = getPort(context);
        int maxConnections = getMaxConnections(context);

        bindingRef.set(getTelnetBinding(context, port, maxConnections));
        bindingRef.get().start();

        System.out.printf(
                "Bundle: %s started with bundle id %d - listening on port %d%n",
                context.getBundle().getSymbolicName(),
                context.getBundle().getBundleId(),
                port
        );
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        bindingRef.get().stop();
        // TODO
    }

    private int getPort(BundleContext context) {
        String portProperty = context.getProperty("org.foo.shell.port");
        int port = 7070;
        if (portProperty != null) {
            port = Integer.parseInt(portProperty);
        }
        return port;
    }

    private int getMaxConnections(BundleContext context) {
        String maxConnectionsProperty = context.getProperty("org.foo.shell.connection.max");

        return maxConnectionsProperty != null ? Integer.parseInt(maxConnectionsProperty) : 4;
    }

    private Command getExecuteCommand(BundleContext context) throws IOException {
        Map<String, Command> commands = new HashMap<>();

        return new HelpCommand();
    }

    private Binding getTelnetBinding(BundleContext context, int port, int maxConnections) throws IOException {
        return new TelnetBinding(getExecuteCommand(context), new ServerSocket(port), maxConnections);
    }
}
