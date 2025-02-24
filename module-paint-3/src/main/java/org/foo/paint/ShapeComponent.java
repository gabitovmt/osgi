package org.foo.paint;

import org.foo.shape.SimpleShape;

import javax.swing.*;
import java.awt.*;

public class ShapeComponent extends JComponent {
    private static final long serialVersionUID = 1L;
    private final PaintFrame frame;
    private final String shapeName;

    public ShapeComponent(PaintFrame frame, String shapeName) {
        this.frame = frame;
        this.shapeName = shapeName;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        SimpleShape shape = frame.getShape(shapeName);
        shape.draw(g2, new Point(getWidth() / 2, getHeight() / 2));
    }
}
