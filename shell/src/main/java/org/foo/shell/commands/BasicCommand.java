package org.foo.shell.commands;

import org.foo.shell.Command;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.framework.wiring.FrameworkWiring;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class BasicCommand implements Command {
    private final AtomicReference<BundleContext> contextRef = new AtomicReference<>();
    private final AtomicReference<String> helpRef = new AtomicReference<>();

    public BasicCommand setContext(BundleContext context) {
        contextRef.set(context);
        return this;
    }

    protected BundleContext getContext() {
        return contextRef.get();
    }

    public BasicCommand setHelp(String help) {
        helpRef.set(help);
        return this;
    }

    protected String getHelp() {
        return helpRef.get();
    }

    protected Bundle getBundle(String id) {
        Bundle bundle = null;
        if (id != null) {
            bundle = getContext().getBundle(Long.parseLong(id.trim()));
        }
        if (bundle == null) {
            throw new IllegalArgumentException("No such bundle: " + id);
        }

        return bundle;
    }

    protected Bundle getSystemBundle() {
        return getBundle("0");
    }

    protected List<Bundle> getBundles() {
        return Arrays.stream(getContext().getBundles())
                .collect(Collectors.toList());
    }

    protected List<Bundle> getBundles(String ids) {
        return Arrays.stream(ids.split("\\s+"))
                .filter(it -> !it.isEmpty())
                .map(this::getBundle)
                .collect(Collectors.toList());
    }

    protected BundleStartLevel getBundleStartLevel(Bundle bundle) {
        return bundle.adapt(BundleStartLevel.class);
    }

    protected FrameworkStartLevel getFrameworkStartLevel() {
        return getSystemBundle().adapt(FrameworkStartLevel.class);
    }

    protected FrameworkWiring getFrameworkWiring() {
        return getSystemBundle().adapt(FrameworkWiring.class);
    }

    @Override
    public String toString() {
        return getHelp() == null ? super.toString() : getHelp();
    }
}
