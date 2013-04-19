/**
 * 
 */
package com.swing.plus.busy.painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Paints a {@link String}.
 */
public class TextPainter extends AbstractPainter {

    private final Font defaultFont;
    private Font font;
    private String text;

    public TextPainter(String text) {
        this(text, null);
    }

    public TextPainter(String text, Font font) {
        super();
        this.text = text;
        this.font = font;
        this.defaultFont = UIManager.getDefaults().getFont("Label.font");
    }

    /**
     * Get the current font value.
     * 
     * @return the current font value;
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Set the font. NB. the invoker must repaint the UI after the invoking this method.
     * 
     * @param font font
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Get the current text value.
     * 
     * @return the current text value;
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set the text. NB. the invoker must repaint the UI after the invoking this method.
     * 
     * @param text text
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void paintLayer(Graphics2D g2, JComponent c, float fade) {
        super.paintLayer(g2, c, fade);
        if (this.text == null || this.text.trim().length() == 0) {
            return;
        }
        int w = c.getWidth();
        int h = c.getHeight();
        float size = Math.min(w, h) / 30;
        // If its too small it can't be seen, set a minimum size. If the minimum size happens to be bigger than the
        // container it will be partially visible, its is expected that
        // this is a rare circumstance.
        int min = 5;
        if (size < min) {
            size = min;
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.white);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fade));
        Font f = this.font != null ? this.font : this.defaultFont.deriveFont(size);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int cx = (w - fm.stringWidth(this.text)) / 2;
        int cy = h / 2;
        g2.drawString(this.text, cx, cy);
    }

}
