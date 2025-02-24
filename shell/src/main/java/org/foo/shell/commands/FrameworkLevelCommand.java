package org.foo.shell.commands;

import java.io.PrintStream;

public class FrameworkLevelCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        if (args == null) {
            out.println(getFrameworkStartLevel().getStartLevel());
        } else {
            getFrameworkStartLevel().setStartLevel(Integer.parseInt(args.trim()));
        }
    }
}
