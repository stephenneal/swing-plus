package com.swing.plus;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PanelFactory {

    private PanelFactory() {
    }

    /**
     * Creates a new panel with {@link BoxLayout} and adds the components.
     * 
     * @param components (optional) components to add
     * @return a new panel with {@link BoxLayout} and the components.
     */
    public static JPanel boxX(JComponent... components) {
        return box(new JPanel(), BoxLayout.X_AXIS, components);
    }

    /**
     * Creates a new panel with {@link BoxLayout} and adds the components.
     * 
     * @param components (optional) components to add
     * @return a new panel with {@link BoxLayout} and the components.
     */
    public static JPanel boxY(JComponent... components) {
        return box(new JPanel(), BoxLayout.Y_AXIS, components);
    }

    /**
     * Creates a new panel with {@link BoxLayout}.
     * 
     * @param panel the panel
     * @return the panel with {@link BoxLayout}.
     */
    public static JPanel boxX(JPanel panel) {
        return box(panel, BoxLayout.X_AXIS);
    }

    /**
     * Creates a new panel with {@link BoxLayout}.
     * 
     * @param panel the panel
     * @return the panel with {@link BoxLayout}.
     */
    public static JPanel boxY(JPanel panel) {
        return box(panel, BoxLayout.Y_AXIS);
    }

    /**
     * Creates a new panel with {@link BoxLayout} and adds the components.
     * 
     * @param axis see {@link BoxLayout#BoxLayout(java.awt.Container, int)} axis
     * @param components (optional) components to add
     * @return a new panel with {@link BoxLayout} and the components.
     */
    public static JPanel box(int axis, JComponent... components) {
        return box(new JPanel(), axis, components);
    }

    /**
     * Sets the layout of the panel to {@link BoxLayout} and adds the components.
     * 
     * @param panel the panel
     * @param axis see {@link BoxLayout#BoxLayout(java.awt.Container, int)} axis
     * @param components (optional) components to add
     * @return the panel with {@link BoxLayout} and the components.
     */
    public static JPanel box(JPanel panel, int axis, JComponent... components) {
        panel.setLayout(new BoxLayout(panel, axis));
        if (components != null) {
            for (JComponent c : components) {
                panel.add(c);
            }
        }
        return panel;
    }

}
