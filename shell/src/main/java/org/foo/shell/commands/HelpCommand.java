package org.foo.shell.commands;

import org.foo.shell.Command;

import java.io.PrintStream;
import java.util.Map;

public class HelpCommand extends BasicCommand {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        for (Command command : commands.values()) {
            out.println(command);
        }
    }
}
