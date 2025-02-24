package org.foo.shell;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryDecorator implements Command, History, FrameworkListener, BundleListener {
    private final List<String> history = Collections.synchronizedList(new ArrayList<>());
    private final Command next;

    public HistoryDecorator(Command next, List<String> history) {
        this.next = next;
        this.history.addAll(history);
    }

    @Override
    public List<String> get() {
        return new ArrayList<>(history);
    }

    @Override
    public void exec(String args, PrintStream out, PrintStream err) throws Exception {
        try {
            next.exec(args, out, err);
        } finally {
            history.add(args);
        }
    }

    @Override
    public void bundleChanged(BundleEvent event) {
        history.add(String.format(
                "    BundleEvent(type=%d,bundle=%s,source=%s)",
                event.getType(), event.getBundle(), event.getSource())
        );
    }

    @Override
    public void frameworkEvent(FrameworkEvent event) {
        history.add(String.format(
                "    FrameworkEvent(type=%d,bundle=%s,source=%s,throwable=%s)",
                event.getType(), event.getBundle(), event.getSource(), event.getThrowable()
        ));
    }
}
