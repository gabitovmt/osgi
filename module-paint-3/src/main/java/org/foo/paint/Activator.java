package org.foo.paint;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Activator implements BundleActivator, Runnable {
    private BundleContext context;
    private PaintFrame frame;
    private ShapeTracker shapeTracker;

    @Override
    public void run() {
        frame = new PaintFrame();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    context.getBundle(0).stop();
                } catch (BundleException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);

        shapeTracker = new ShapeTracker(context, frame);
        shapeTracker.open();
    }

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        if (SwingUtilities.isEventDispatchThread()) {
            run();
        } else {
            try {
                SwingUtilities.invokeAndWait(this);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    @Override
    public void stop(BundleContext context) {
        shapeTracker.close();
        PaintFrame thisFrame = frame;
        SwingUtilities.invokeLater(() -> {
            thisFrame.setVisible(false);
            thisFrame.dispose();
        });
    }
}
