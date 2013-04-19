/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus.mvc;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.jxlayer.JXLayer;

import com.swing.binding.bbb.BindingService;
import com.swing.binding.bbb.mvc.AbstractPanel;
import com.swing.binding.bbb.mvc.PresentationModel;
import com.swing.plus.busy.AnimatedUI;
import com.swing.plus.busy.painter.DimPainter;
import com.swing.plus.busy.painter.SpinningWheelPainter;

/**
 * {@link JPanel} that extends {@link AbstractPanel} to provide functionality such as a {@link JXLayer}. NB. Containers
 * that want to display the {@link JXLayer} for a panel instance must add the {@link JXLayer} not the {@link JPanel}.
 * 
 * @author steve
 * @since 20/04/2013
 */
@SuppressWarnings("serial")
public class PlusJPanel<M extends PresentationModel> extends AbstractPanel<M> {

    private final JXLayer<PlusJPanel<M>> jxLayer;

    public PlusJPanel(M model, BindingService bindingService) {
        super(model, bindingService);
        JXLayer<PlusJPanel<M>> l = new JXLayer<PlusJPanel<M>>(this);
        l.setUI(new AnimatedUI(new DimPainter(), new SpinningWheelPainter()));
        this.jxLayer = l;
    }

    /**
     * Get the {@link JXLayer} for this instance. Containers must add the {@link JXLayer} (not the panel) to display it.
     * 
     * @return the {@link JXLayer} for this instance
     */
    public JXLayer<PlusJPanel<M>> getJxLayer() {
        return this.jxLayer;
    }

    protected static JLabel newFieldLabel() {
        JLabel l = new JLabel();
        // l.setBorder(BorderFactory.createEtchedBorder());
        return l;
    }
}
