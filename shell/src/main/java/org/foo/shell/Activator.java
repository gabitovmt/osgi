package org.foo.shell;

import org.foo.shell.commands.*;
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
        commands.put("help", new HelpCommand(commands).setContext(context)
                .setHelp("help - display commands."));
        commands.put("install", new InstallCommand().setContext(context)
                .setHelp("install <url> - Install the bundle jar at the given url."));
        commands.put("start", new StartCommand().setContext(context)
                .setHelp("start <id> - Start the bundle with the given bundle id."));
        commands.put("stop", new StopCommand().setContext(context)
                .setHelp("stop <id> - Stop the bundle with the given bundle id."));
        commands.put("uninstall", new UninstallCommand().setContext(context)
                .setHelp("uninstall <id> - Uninstall the bundle with the given bundle id."));
        commands.put("update", new UpdateCommand().setContext(context)
                .setHelp("update <id> - Update the bundle with the given bundle id."));
        commands.put("startlevel", new FrameworkLevelCommand().setContext(context)
                .setHelp("startlevel [<level>] - Get or set the framework startlevel."));
        commands.put("bundlelevel", new BundleLevelCommand().setContext(context)
                .setHelp("bundlelevel [<level>] <id> - Get or set bundle startlevel."));
        commands.put("bundles", new BundlesCommand().setContext(context)
                .setHelp("bundles - Print information about the currently installed bundles"));

        return new ExecuteCommand(commands);
    }

    private Binding getTelnetBinding(BundleContext context, int port, int maxConnections) throws IOException {
        return new TelnetBinding(getExecuteCommand(context), new ServerSocket(port), maxConnections);
    }
}
