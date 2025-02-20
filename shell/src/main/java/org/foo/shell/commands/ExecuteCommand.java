package org.foo.shell.commands;

import org.foo.shell.Command;

import java.io.PrintStream;
import java.util.Map;

public class ExecuteCommand implements Command {
    private final Map<String, Command> commands;

    public ExecuteCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void exec(String args, PrintStream out, PrintStream err) {
        int idx = args.indexOf(' ');

        boolean found = false;
        Command command = commands.get((idx > 0) ? args.substring(0, idx) : args);

        if (command != null) {
            found = true;

            try {
                command.exec((idx > 0) ? args.substring(idx) : null, out, err);
            } catch (Exception ex) {
                ex.printStackTrace(err);
                out.println("Unable to execute: " + args);
            }
        }

        if (!found && !(args.trim().isEmpty())) {
            out.println("Unknown command: " + args);
        }
    }
}
