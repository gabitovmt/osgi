package org.foo.paint;

import org.foo.shape.SimpleShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PaintFrame extends JFrame implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private static final int BOX = 54;
    private final JToolBar toolbar;
    private String selected;
    private final JPanel panel;
    private ShapeComponent selectedComponent;
    private final Map<String, ShapeInfo> shapes = new HashMap<>();
    private final ActionListener reusableActionListener = new ShapeActionListener();
    private final transient SimpleShape defaultShape = new DefaultShape();

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

    public void selectShape(String name) {
        selected = name;
    }

    public SimpleShape getShape(String name) {
        ShapeInfo info = shapes.get(name);
        return info == null ? defaultShape : info.shape;
    }

    public void addShape(String name, Icon icon, SimpleShape shape) {
        shapes.put(name, new ShapeInfo(name, icon, shape));
        JButton button = new JButton(icon);
        button.setActionCommand(name);
        button.setToolTipText(name);
        button.addActionListener(reusableActionListener);

        if (selected == null) {
            button.doClick();
        }

        toolbar.add(button);
        toolbar.validate();
        repaint();
    }

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

    public void mousePressed(MouseEvent evt) {
        Component c = panel.getComponentAt(evt.getPoint());
        if (c instanceof ShapeComponent) {
            selectedComponent = (ShapeComponent) c;
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            panel.addMouseMotionListener(this);
            selectedComponent.repaint();
        }
    }

    public void mouseReleased(MouseEvent evt) {
        if (selectedComponent != null) {
            panel.removeMouseMotionListener(this);
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            selectedComponent.setBounds(evt.getX() - BOX / 2, evt.getY() - BOX / 2, BOX, BOX);
            selectedComponent.repaint();
            selectedComponent = null;
        }
    }

    public void mouseDragged(MouseEvent evt) {
        selectedComponent.setBounds(evt.getX() - BOX / 2, evt.getY() - BOX / 2, BOX, BOX);
    }

    public void mouseMoved(MouseEvent evt) {
        // empty
    }

    private class ShapeActionListener implements ActionListener, Serializable {
        public void actionPerformed(ActionEvent evt) {
            selectShape(evt.getActionCommand());
        }
    }

    private static class ShapeInfo {
        public final String name;
        public final Icon icon;
        public final SimpleShape shape;

        public ShapeInfo(String name, Icon icon, SimpleShape shape) {
            this.name = name;
            this.icon = icon;
            this.shape = shape;
        }
    }
}
