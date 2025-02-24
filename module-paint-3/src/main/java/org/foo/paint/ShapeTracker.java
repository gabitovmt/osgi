package org.foo.paint;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.BundleTracker;

public class ShapeTracker extends BundleTracker<Bundle> {

    public ShapeTracker(BundleContext context, PaintFrame frame) {
        super(context, Bundle.ACTIVE | Bundle.STOPPING, new ShapeTrackerCustomizer(context, frame));
    }
}
