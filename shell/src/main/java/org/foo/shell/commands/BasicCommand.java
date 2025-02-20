package org.foo.shell.commands;

import org.foo.shell.Command;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BasicCommand implements Command {
    private final AtomicReference<BundleContext> contextRef = new AtomicReference<>();
    private final AtomicReference<String> helpRef = new AtomicReference<>();

    public BasicCommand setContext(BundleContext context) {
        contextRef.set(context);
        return this;
    }

    public BundleContext getContext() {
        return contextRef.get();
    }

    public BasicCommand setHelp(String help) {
        helpRef.set(help);
        return this;
    }

    public String getHelp() {
        return helpRef.get();
    }

    public Bundle getBundle(String id) {
        Bundle bundle = null;
        if (id != null) {
            bundle = getContext().getBundle(Long.parseLong(id.trim()));
        }
        if (bundle == null) {
            throw new IllegalArgumentException("No such bundle: " + id);
        }

        return bundle;
    }

    @Override
    public String toString() {
        return getHelp() == null ? super.toString() : getHelp();
    }
}
