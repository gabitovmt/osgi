package org.foo.shell;

import java.net.ServerSocket;

public class TelnetBinding implements Runnable, Binding {
    private final Command command;
    private final ServerSocket socket;
    private final int maxConnections;
    private final Thread thread = new Thread(this);

    public TelnetBinding(Command command, ServerSocket socket, int maxConnections) {
        this.command = command;
        this.socket = socket;
        this.maxConnections = maxConnections;
    }

    @Override
    public void run() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() throws InterruptedException {

    }
}
