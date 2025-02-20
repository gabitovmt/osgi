package org.foo.shell.commands;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import java.io.PrintStream;

public class StopCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        Bundle bundle = getBundle(args);

        if (bundle.equals(getContext().getBundle())) {
            new SelfStopThread(bundle).start();
        } else {
            bundle.stop();
        }
    }

    private static final class SelfStopThread extends Thread {
        private final Bundle self;

        public SelfStopThread(Bundle self) {
            super("SelfStopThread Bundle " + self.getBundleId());
            this.self = self;
        }

        @Override
        public void run() {
            try {
                self.stop();
            } catch (BundleException ex) {
                // Ignore
            }
        }
    }
}
