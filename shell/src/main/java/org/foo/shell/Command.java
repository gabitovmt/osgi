package org.foo.shell;

import java.io.PrintStream;

public interface Command {
    void exec(String args, PrintStream out, PrintStream err) throws Exception;
}
