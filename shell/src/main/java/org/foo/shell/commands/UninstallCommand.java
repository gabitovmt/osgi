package org.foo.shell.commands;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import java.io.PrintStream;

public class UninstallCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        Bundle bundle = getBundle(args);

        if (bundle.equals(getContext().getBundle())) {
            new SelfUninstallThread(bundle).start();
        } else {
            bundle.uninstall();
        }
    }

    private static final class SelfUninstallThread extends Thread {
        private final Bundle self;

        public SelfUninstallThread(Bundle self) {
            super("SelfUninstallThread Bundle " + self.getBundleId());
            this.self = self;
        }

        @Override
        public void run() {
            try {
                self.uninstall();
            } catch (BundleException e) {
                // Ignore
            }
        }
    }
}
