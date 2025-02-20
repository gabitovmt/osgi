package org.foo.shell.commands;

import java.io.PrintStream;

public class StartCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        getBundle(args).start();
    }
}
