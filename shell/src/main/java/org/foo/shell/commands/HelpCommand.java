package org.foo.shell.commands;

import org.foo.shell.Command;

import java.io.PrintStream;

public class HelpCommand implements Command {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        out.println("Help!");
    }
}
