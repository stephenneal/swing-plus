package com.swing.plus.busy.painter;

import java.awt.Graphics2D;

import javax.swing.JComponent;

public abstract class AbstractPainter implements Painter {

    @Override
    public void paintLayer(Graphics2D g2, JComponent c) {
        paintLayer(g2, c, 1f);
    }

    @Override
    public void paintLayer(Graphics2D g2, JComponent c, float fade) {
    }

    @Override
    public void reset() {
    }

}
