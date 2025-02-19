package org.foo.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TelnetBinding implements Runnable, Binding {
    private final Command command;
    private final ServerSocket socket;
    private final int maxConnections;
    private final Thread thread = new Thread(this);
    private final List<StoppableShellThread> threads = new ArrayList<>();

    public TelnetBinding(Command command, ServerSocket socket, int maxConnections) {
        this.command = command;
        this.socket = socket;
        this.maxConnections = maxConnections;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Socket acceptedSocket = acceptSocket();
            if (acceptedSocket == null) {
                break;
            }

            Shell shell;
            if (hasFreeConnection() && ((shell = newShell(acceptedSocket)) != null)) {
                new StoppableShellThread(shell, acceptedSocket).start();
            }
        }

        stopAndWait();
    }

    private Socket acceptSocket() {
        try {
            return this.socket.accept();
        } catch (IOException ex) {
            if (Thread.interrupted()) {
                return null;
            }
            throw new ShellException(ex);
        }
    }

    private boolean hasFreeConnection() {
        synchronized (threads) {
            if (threads.size() >= maxConnections) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // ignore
                }

                return false;
            }
        }

        return true;
    }

    private Shell newShell(Socket socket) {
        try {
            return new Shell(
                    command,
                    new BufferedReader(new InputStreamReader(socket.getInputStream())),
                    new PrintStream(socket.getOutputStream()),
                    new PrintStream(socket.getOutputStream())
            );
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException ioEx) {
                // ignore
            }

            return null;
        }
    }

    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void stop() throws InterruptedException {
        thread.interrupt();

        try {
            socket.close();
        } catch (IOException ex) {
            // ignore
        }

        thread.join();
    }

    private void stopAndWait() {
        List<StoppableShellThread> shellThreads;
        synchronized (threads) {
            shellThreads = new ArrayList<>(threads);
        }

        for (StoppableShellThread shellThread : shellThreads) {
            try {
                shellThread.stopAndWait();
            } catch (InterruptedException ex) {
                // ignore
                Thread.currentThread().interrupt();
            }
        }

        Thread.currentThread().interrupt();
    }

    private final class StoppableShellThread extends Thread {
        private final Socket socket;

        public StoppableShellThread(Shell shell, Socket socket) {
            super(shell);
            this.socket = socket;
        }

        @Override
        public synchronized void start() {
            synchronized (threads) {
                threads.add(this);
            }

            super.start();
        }

        void stopAndWait() throws InterruptedException {
            if (!isAlive()) {
                return;
            }

            interrupt();
            try {
                socket.close();
            } catch (IOException ex) {
                // ignore
            }

            join();
        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                synchronized (threads) {
                    threads.remove(this);
                }
            }
        }
    }
}
