package org.foo.shell.commands;

import java.io.PrintStream;

public class RefreshCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        if (args == null) {
            getFrameworkWiring().refreshBundles(null);
        } else {
            getFrameworkWiring().refreshBundles(getBundles(args));
        }
    }
}
