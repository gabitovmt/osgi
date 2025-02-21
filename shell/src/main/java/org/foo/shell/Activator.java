package org.foo.shell;

import org.foo.shell.commands.*;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Activator implements BundleActivator {
    private final AtomicReference<Binding> bindingRef = new AtomicReference<>();
    private final AtomicReference<History> historyRef = new AtomicReference<>();

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
        writeHistory(historyRef.get(), context);
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
        Map<String, Command> commands = new LinkedHashMap<>();
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
        commands.put("refresh", new RefreshCommand().setContext(context)
                .setHelp("refresh [<id> ...] - refresh bundles."));
        commands.put("resolve", new ResolveCommand().setContext(context)
                .setHelp("resolve [<id> ...] - resolve bundles."));
        commands.put("bundles", new BundlesCommand().setContext(context)
                .setHelp("bundles - Print information about the currently installed bundles"));

        HistoryDecorator historyDecorator = new HistoryDecorator(new ExecuteCommand(commands), readHistory(context));
        context.addFrameworkListener(historyDecorator);
        context.addBundleListener(historyDecorator);
        historyRef.set(historyDecorator);

        return historyDecorator;
    }

    private Binding getTelnetBinding(BundleContext context, int port, int maxConnections) throws IOException {
        return new TelnetBinding(getExecuteCommand(context), new ServerSocket(port), maxConnections);
    }

    private List<String> readHistory(BundleContext context) throws IOException {
        File log = getFileLog(context);

        return log.isFile()
                ? Files.readAllLines(log.toPath())
                : new ArrayList<>();
    }

    private void writeHistory(History history, BundleContext context) throws IOException {
        File log = getFileLog(context);
        Files.write(log.toPath(), history.get(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private File getFileLog(BundleContext context) {
        return context.getDataFile("log.txt");
    }
}
