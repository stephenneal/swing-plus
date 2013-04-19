/**
 * 
 */
package com.swing.plus.busy.painter;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * Paints a spinning wheel reminiscent of the iPhone wait indicator.
 */
public class SpinningWheelPainter extends AbstractPainter {

    private int angle;

    @Override
    public void paintLayer(Graphics2D g2, JComponent c, float fade) {
        super.paintLayer(g2, c, fade);
        int w = c.getWidth();
        int h = c.getHeight();
        // Paint the wait indicator.
        int strokes = 16;
        int size = Math.min(w, h) / 30;
        // If its too small it can't be seen, set a minimum size. If the minimum size happens to be bigger than the
        // container it will be partially visible, its is expected that
        // this is a rare circumstance.
        int min = 5;
        if (size < min) {
            size = min;
            strokes = 12;
        }
        int cx = w / 2;
        int cy = h / 2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(size / 4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setPaint(Color.white);
        for (int i = 0; i < this.angle; i++) {
            g2.rotate(Math.PI / (strokes / 2), cx, cy);
        }
        if (++this.angle >= strokes) {
            this.angle = 0;
        }
        float strokesLess1 = strokes - 1.0f;
        for (int i = 0; i < strokes; i++) {
            float scale = (strokesLess1 - i) / strokesLess1;
            g2.drawLine(cx + size, cy, cx + size * 2, cy);
            g2.rotate(Math.PI / (strokes / 2), cx, cy);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, scale * fade));
        }
    }

    @Override
    public void reset() {
        this.angle = 0;
    }

}
