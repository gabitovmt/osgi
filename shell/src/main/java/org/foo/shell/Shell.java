package org.foo.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class Shell implements Runnable {
    private final Command command;
    private final BufferedReader in;
    private final PrintStream out;
    private final PrintStream err;

    public Shell(Command command, BufferedReader in, PrintStream out, PrintStream err) {
        this.command = command;
        this.in = in;
        this.out = out;
        this.err = err;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            out.print("-> ");

            String cmdLine;
            try {
                cmdLine = in.readLine();
            } catch (IOException ex) {
                if (!Thread.currentThread().isInterrupted()) {
                    ex.printStackTrace(err);
                    err.println("Unable to read from stdin - exiting now");
                }
                return;
            }

            if (cmdLine == null) {
                out.println("Bye bye");
                return;
            }

            try {
                command.exec(cmdLine, out, err);
            } catch (Exception ex) {
                ex.printStackTrace(err);
            }
        }
    }
}
