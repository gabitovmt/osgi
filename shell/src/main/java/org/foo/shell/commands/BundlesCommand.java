package org.foo.shell.commands;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import java.io.PrintStream;

public class BundlesCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        out.println("ID  |State    |Name");
        for (Bundle bundle : getContext().getBundles()) {
            printBundle(
                    bundle.getBundleId(),
                    getStateString(bundle.getState()),
                    bundle.getHeaders().get(Constants.BUNDLE_NAME),
                    bundle.getLocation(),
                    bundle.getSymbolicName(),
                    out
            );
        }
    }

    private String getStateString(int state) {
        switch (state) {
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.STOPPING:
                return "STOPPING";
            default:
                return "UNKNOWN";
        }
    }

    private void printBundle(
            long id, String state, String name, String location, String symbolicName, PrintStream out
    ) {
        out.printf("%4d|%-9s|%s%n", id, state, name);
        out.println("Symbolic Name: " + symbolicName);
        out.println("Location: " + location);
    }
}
