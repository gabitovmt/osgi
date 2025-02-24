package org.foo.shell.commands;

import java.io.PrintStream;

public class ResolveCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        boolean success = getFrameworkWiring()
                .resolveBundles(args == null ? null : getBundles(args));
        out.println(success ? "Success" : "Failure");
    }
}
