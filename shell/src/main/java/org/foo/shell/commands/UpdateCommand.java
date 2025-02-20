package org.foo.shell.commands;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import java.io.PrintStream;

public class UpdateCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        Bundle bundle = getBundle(args);

        if (bundle.equals(getContext().getBundle())) {
            new SelfUpdateThread(bundle).start();
        } else {
            bundle.update();
        }
    }

    private static final class SelfUpdateThread extends Thread {
        private final Bundle self;

        public SelfUpdateThread(Bundle self) {
            super("SelfUpdateThread Bundle " + self.getBundleId());
            this.self = self;
        }

        @Override
        public void run() {
            try {
                self.update();
            } catch (BundleException e) {
                // Ignore
            }
        }
    }
}
