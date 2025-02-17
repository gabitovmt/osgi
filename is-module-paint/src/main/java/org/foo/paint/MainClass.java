package org.foo.paint;

import org.foo.shape.circle.Circle;
import org.foo.shape.square.Square;
import org.foo.shape.triangle.Triangle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainClass implements BundleActivator {

    /**
     * This method actually performs the creation of the application window. It is
     * intended to be called by the Swing event thread and should not be called
     * directly.
     **/
    @Override
    public void start(BundleContext context) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            PaintFrame frame = new PaintFrame();
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent evt) {
                    System.exit(0);
                }
            });
            frame.addShape(new Circle());
            frame.addShape(new Square());
            frame.addShape(new Triangle());
            frame.setVisible(true);
        });
    }

    @Override
    public void stop(BundleContext context) {
        // не требуется
    }
}
