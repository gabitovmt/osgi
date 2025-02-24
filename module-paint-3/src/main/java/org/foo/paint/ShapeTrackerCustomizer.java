package org.foo.paint;

import org.foo.shape.SimpleShape;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import javax.swing.*;
import java.util.Dictionary;

public class ShapeTrackerCustomizer implements BundleTrackerCustomizer<Bundle> {
    private final BundleContext context;
    private final PaintFrame frame;

    public ShapeTrackerCustomizer(BundleContext context, PaintFrame frame) {
        this.context = context;
        this.frame = frame;
    }

    @Override
    public Bundle addingBundle(Bundle bundle, BundleEvent event) {
        processBundleOnEventThread(Action.ADDED, bundle);
        return bundle;
    }

    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Bundle object) {
        // ignore
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Bundle object) {
        processBundleOnEventThread(Action.REMOVED, bundle);
    }

    private void processBundleOnEventThread(Action action, Bundle bundle) {
        if ((context.getBundle(0).getState() & (Bundle.ACTIVE | Bundle.STOPPING)) == 0) {
            return;
        }

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                processBundle(action, bundle);
            } else {
                SwingUtilities.invokeAndWait(new BundleRunnable(action, bundle));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PaintException(ex);
        }
    }

    private void processBundle(Action action, Bundle bundle) {
        Dictionary<String, String> dict = bundle.getHeaders();

        String name = dict.get(SimpleShape.NAME_PROPERTY);
        if (name == null) {
            return;
        }

        switch (action) {
            case ADDED:
                String iconPath = dict.get(SimpleShape.ICON_PROPERTY);
                Icon icon = new ImageIcon(bundle.getResource(iconPath));
                String className = dict.get(SimpleShape.CLASS_PROPERTY);
                frame.addShape(name, icon, new DefaultShape(context, bundle.getBundleId(), className));
                break;
            case REMOVED:
                frame.removeShape(name);
                break;
            default:
                throw new PaintException("Unsupported operation");
        }
    }

    private enum Action {
        ADDED, REMOVED
    }

    private class BundleRunnable implements Runnable {
        private final Action action;
        private final Bundle bundle;

        public BundleRunnable(Action action, Bundle bundle) {
            this.action = action;
            this.bundle = bundle;
        }

        public void run() {
            processBundle(action, bundle);
        }
    }
}
