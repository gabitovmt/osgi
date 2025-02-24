package org.foo.shape.triangle;

import org.foo.shape.SimpleShape;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class Triangle implements SimpleShape {

    @Override
    public void draw(Graphics2D g2, Point p) {
        int x = p.x - 25;
        int y = p.y - 25;
        GradientPaint gradient = new GradientPaint(x, y, Color.GREEN, (float) x + 50, y, Color.WHITE);
        g2.setPaint(gradient);
        int[] xcoords = {x + 25, x, x + 50};
        int[] ycoords = {y, y + 50, y + 50};
        GeneralPath polygon = new GeneralPath(Path2D.WIND_EVEN_ODD, xcoords.length);
        polygon.moveTo((float) x + 25, y);
        for (int i = 0; i < xcoords.length; i++) {
            polygon.lineTo(xcoords[i], ycoords[i]);
        }
        polygon.closePath();
        g2.fill(polygon);
        BasicStroke wideStroke = new BasicStroke(2.0f);
        g2.setColor(Color.black);
        g2.setStroke(wideStroke);
        g2.draw(polygon);
    }
}
