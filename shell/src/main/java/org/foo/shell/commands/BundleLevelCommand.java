package org.foo.shell.commands;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.startlevel.BundleStartLevel;

import java.io.PrintStream;
import java.util.StringTokenizer;

public class BundleLevelCommand extends BasicCommand {

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        StringTokenizer tok = new StringTokenizer(args);
        if (tok.countTokens() == 1) {
            Bundle bundle = getBundle(tok.nextToken());
            int level = getBundleStartLevel(bundle).getStartLevel();
            out.println("Bundle " + args + " has level " + level);
        } else {
            int level = Integer.parseInt(tok.nextToken());
            Bundle bundle = getBundle(tok.nextToken());
            getBundleStartLevel(bundle).setStartLevel(level);
        }
    }

    private BundleStartLevel getBundleStartLevel(Bundle bundle) {
        BundleContext context = bundle.getBundleContext();
        return context.getService(context.getServiceReference(BundleStartLevel.class));
    }
}
