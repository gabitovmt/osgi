package org.foo.shell.commands;

import org.osgi.framework.Bundle;

import java.io.PrintStream;

public class InstallCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        Bundle bundle = getContext().installBundle(args);
        out.println("Bundle: " + bundle.getBundleId());
    }
}
