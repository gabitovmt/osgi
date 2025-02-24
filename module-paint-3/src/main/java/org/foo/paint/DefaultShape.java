package org.foo.paint;

import org.foo.shape.SimpleShape;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DefaultShape implements SimpleShape {
    private SimpleShape shape;
    private ImageIcon icon;
    private BundleContext context;
    private long bundleId;
    private String className;

    public DefaultShape() {
    }

    public DefaultShape(BundleContext context, long bundleId, String className) {
        this.context = context;
        this.bundleId = bundleId;
        this.className = className;
    }

    @Override
    public void draw(Graphics2D g2, Point p) {
        if (context != null) {
            try {
                if (shape == null) {
                    Bundle bundle = context.getBundle(bundleId);
                    Class<?> clazz = bundle.loadClass(className);
                    shape = (SimpleShape) clazz.newInstance();
                }

                shape.draw(g2, p);
                return;
            } catch (Exception ex) {
                // ignore
            }
        }

        if (icon == null) {
            try {
                icon = new ImageIcon(Objects.requireNonNull(
                        this.getClass().getClassLoader().getResource("underc.png"))
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                g2.setColor(Color.red);
                g2.fillRect(0, 0, 60, 60);
                return;
            }
        }

        g2.drawImage(icon.getImage(), 0, 0, null);
    }
}
