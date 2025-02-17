/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.foo.paint;

import org.foo.shape.SimpleShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas. This class does not
 * directly interact with the underlying OSGi framework; instead, it is injected
 * with the available <tt>SimpleShape</tt> instances to eliminate any
 * dependencies on the OSGi application programming interfaces.
 **/
public class PaintFrame extends JFrame implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private static final int BOX = 54;
    private final JToolBar toolbar;
    private String selected;
    private final JPanel panel;
    private ShapeComponent selectedComponent;
    private final Map<String, SimpleShape> shapes = new HashMap<>();
    private final ActionListener reusableActionListener = new ShapeActionListener();

    /**
     * Default constructor that populates the main window.
     **/
    public PaintFrame() {
        super("PaintFrame");

        toolbar = new JToolBar("Toolbar");
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.setMinimumSize(new Dimension(400, 400));
        panel.addMouseListener(this);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(400, 400);
    }

    /**
     * This method sets the currently selected shape to be used for drawing on the
     * canvas.
     *
     * @param name The name of the shape to use for drawing on the canvas.
     **/
    public void selectShape(String name) {
        selected = name;
    }

    /**
     * Retrieves the available <tt>SimpleShape</tt> associated with the given
     * name.
     *
     * @param name The name of the <tt>SimpleShape</tt> to retrieve.
     * @return The corresponding <tt>SimpleShape</tt> instance if available or
     * <tt>null</tt>.
     **/
    public SimpleShape getShape(String name) {
        return shapes.get(name);
    }

    /**
     * Injects an available <tt>SimpleShape</tt> into the drawing frame.
     * The name of the injected <tt>SimpleShape</tt>.
     * The icon associated with the injected <tt>SimpleShape</tt>.
     *
     * @param shape The injected <tt>SimpleShape</tt> instance.
     **/
    public void addShape(SimpleShape shape) {
        shapes.put(shape.getName(), shape);
        JButton button = new JButton(shape.getIcon());
        button.setActionCommand(shape.getName());
        button.setToolTipText(shape.getName());
        button.addActionListener(reusableActionListener);

        if (selected == null) {
            button.doClick();
        }

        toolbar.add(button);
        toolbar.validate();
        repaint();
    }

    /**
     * Removes a no longer available <tt>SimpleShape</tt> from the drawing frame.
     *
     * @param name The name of the <tt>SimpleShape</tt> to remove.
     **/
    public void removeShape(String name) {
        shapes.remove(name);

        if ((selected != null) && selected.equals(name)) {
            selected = null;
        }

        for (int i = 0; i < toolbar.getComponentCount(); i++) {
            JButton sb = (JButton) toolbar.getComponent(i);
            if (sb.getActionCommand().equals(name)) {
                toolbar.remove(i);
                toolbar.invalidate();
                validate();
                repaint();
                break;
            }
        }

        if ((selected == null) && (toolbar.getComponentCount() > 0)) {
            ((JButton) toolbar.getComponent(0)).doClick();
        }
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to draw the
     * selected shape into the drawing canvas.
     *
     * @param evt The associated mouse event.
     **/
    @Override
    public void mouseClicked(MouseEvent evt) {
        if (selected == null) {
            return;
        }

        if (panel.contains(evt.getX(), evt.getY())) {
            ShapeComponent sc = new ShapeComponent(this, selected);
            sc.setBounds(evt.getX() - BOX / 2, evt.getY() - BOX / 2, BOX, BOX);
            panel.add(sc, 0);
            panel.validate();
            panel.repaint(sc.getBounds());
        }
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        // empty
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        // empty
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     *
     * @param evt The associated mouse event.
     **/
    public void mousePressed(MouseEvent evt) {
        Component c = panel.getComponentAt(evt.getPoint());
        if (c instanceof ShapeComponent) {
            selectedComponent = (ShapeComponent) c;
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            panel.addMouseMotionListener(this);
            selectedComponent.repaint();
        }
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     *
     * @param evt The associated mouse event.
     **/
    public void mouseReleased(MouseEvent evt) {
        if (selectedComponent != null) {
            panel.removeMouseMotionListener(this);
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            selectedComponent.setBounds(evt.getX() - BOX / 2, evt.getY() - BOX / 2, BOX, BOX);
            selectedComponent.repaint();
            selectedComponent = null;
        }
    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to move a
     * dragged shape.
     *
     * @param evt The associated mouse event.
     **/
    public void mouseDragged(MouseEvent evt) {
        selectedComponent.setBounds(evt.getX() - BOX / 2, evt.getY() - BOX / 2, BOX, BOX);
    }

    public void mouseMoved(MouseEvent evt) {
        // empty
    }

    /**
     * Simple action listener for shape toolbar buttons that sets the drawing
     * frame's currently selected shape when receiving an action event.
     **/
    private class ShapeActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent evt) {
            selectShape(evt.getActionCommand());
        }
    }
}
