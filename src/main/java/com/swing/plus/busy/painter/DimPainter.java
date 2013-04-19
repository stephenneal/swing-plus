/**
 * 
 */
package com.swing.plus.busy.painter;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Paints a dim layer.
 */
public class DimPainter extends AbstractPainter {

    @Override
    public void paintLayer(Graphics2D g2, JComponent c, float fade) {
        super.paintLayer(g2, c, fade);
        int w = c.getWidth();
        int h = c.getHeight();
        // Gray it out.
        Composite urComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f * fade));
        g2.fillRect(0, 0, w, h);
        g2.setComposite(urComposite);
    }

}
