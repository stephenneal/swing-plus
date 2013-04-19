package com.swing.plus.busy.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.swing.plus.busy.AnimatedUI;


/**
 * Perform custom painting. For use with {@link AnimatedUI}.
 */
public interface Painter {

    /**
     * Paint the layer using the graphics passed.
     * 
     * @param g2
     * @param c
     */
    void paintLayer(Graphics2D g2, JComponent c);

    /**
     * Paint the layer using the graphics passed.
     * 
     * @param g2
     * @param width
     * @param c
     */
    void paintLayer(Graphics2D g2, JComponent c, float fade);

    /**
     * Reset the painter.
     */
    void reset();

}
