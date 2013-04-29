/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus.mvc;

import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.jxlayer.JXLayer;

import com.swing.binding.bbb.BindingService;
import com.swing.binding.bbb.mvc.PresentationModel;
import com.swing.plus.busy.AnimatedUI;
import com.swing.plus.busy.painter.DimPainter;
import com.swing.plus.busy.painter.SpinningWheelPainter;

/**
 * Abstract implementation of a {@link JPanel} that provides base functionality for an MVC framework using the Swing binding. It also adds {@link JXLayer} functionality to each
 * panel instance. NB. {@link Container}'s that want to display the {@link JXLayer} for a panel instance must add the {@link JXLayer} not the {@link JPanel}.
 * 
 * @author Stephen Neal
 * @since 20/04/2013
 */
@SuppressWarnings("serial")
public class PlusJPanel<M extends PresentationModel> extends JPanel {

    private final JXLayer<PlusJPanel<M>> jxLayer;

    private BindingService bindingService;
    private M model;

    /**
     * Default constructor. Use this when binding is not to be used, any calls to {@link #bind(Binding)} on this
     * instance will throw an {@link UnsupportedOperationException}.
     */
    public PlusJPanel() {
        this(null);
    }

    /**
     * Default constructor. Use this when binding is not to be used, any calls to {@link #bind(Binding)} on this
     * instance will throw an {@link UnsupportedOperationException}.
     */
    public PlusJPanel(M model) {
        super();
        this.model = model;
        JXLayer<PlusJPanel<M>> l = new JXLayer<PlusJPanel<M>>(this);
        l.setUI(new AnimatedUI(new DimPainter(), new SpinningWheelPainter()));
        this.jxLayer = l;
    }

    /**
     * Constructor that sets the model instance and the binding service.
     */
    public PlusJPanel(M model, BindingService bindingService) {
        this(model);
        this.bindingService = bindingService;
        if (model != null) {
            // TODO replace with binding for title and border
            TitleListener listener = new TitleListener();
            getModel().addPropertyChangeListener("title", listener);
            listener.propertyChange(new PropertyChangeEvent(model, "title", null, model.getTitle()));
        }
    }

    public M getModel() {
        return this.model;
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

    protected void bind(Binding<?, ?, ?, ?>... bindings) {
        if (bindings == null) {
            return;
        }
        bind(Arrays.asList(bindings));
    }

    protected void bind(List<Binding<?, ?, ?, ?>> bindings) {
        if (bindings == null) {
            return;
        }
        if (this.bindingService == null) {
            throw new UnsupportedOperationException("binding not supported, no binding service");
        }
        for (Binding<?, ?, ?, ?> b : bindings) {
            this.bindingService.bind(b);
        }
    }

    private class TitleListener implements PropertyChangeListener {
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Border border = getBorder();
                    TitledBorder t = null;
                    if (border instanceof TitledBorder) {
                        t = (TitledBorder) border;
                    } else if (border instanceof CompoundBorder) {
                        CompoundBorder c = (CompoundBorder) border;
                        if (c.getOutsideBorder() instanceof TitledBorder) {
                            t = (TitledBorder) c.getOutsideBorder();
                        } else if (c.getInsideBorder() instanceof TitledBorder) {
                            t = (TitledBorder) c.getInsideBorder();
                        }
                    }
                    if (t != null) {
                        t.setTitle((String) evt.getNewValue());
                    }
                }
            });
        }
    }
}