package org.foo.shape;

import java.awt.*;

public interface SimpleShape {

    String NAME_PROPERTY = "Extension-Name";
    String ICON_PROPERTY = "Extension-Icon";
    String CLASS_PROPERTY = "Extension-Class";

    void draw(Graphics2D g2, Point p);
}
