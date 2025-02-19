package org.foo.shell;

import java.io.BufferedReader;
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
        // TODO
    }
}
