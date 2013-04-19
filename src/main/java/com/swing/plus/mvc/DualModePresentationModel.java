/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swing.plus.mvc;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swing.binding.bbb.mvc.PresentationModel;

/**
 * A presentation model for a view that can operate in normal or read only mode.
 * 
 * @author steve
 * @since 20/04/2013
 */
public class DualModePresentationModel extends PresentationModel {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DualModePresentationModel.class);

    public enum ViewMode {
        DEFAULT, READ_ONLY
    }

    private ViewMode mode;

    /**
     * Default constructor.
     */
    public DualModePresentationModel() {
        super();
        this.mode = ViewMode.DEFAULT;
        ModePropertyChangeListener l = new ModePropertyChangeListener();
        getPropertyChangeSupport().addPropertyChangeListener("mode", l);
        l.propertyChange(new PropertyChangeEvent(this, "mode", null, this.mode));
    }

    public ViewMode getMode() {
        return this.mode;
    }

    public void setMode(ViewMode newValue) {
        if (newValue == null) {
            newValue = ViewMode.DEFAULT;
        }
        ViewMode oldValue = this.mode;
        this.mode = newValue;
        getPropertyChangeSupport().firePropertyChange("mode", oldValue, newValue);
    }

    private class ModePropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            synchronized (DualModePresentationModel.this) {
                Class<? extends DualModePresentationModel> c = DualModePresentationModel.this.getClass();
                try {
                    BeanInfo info = Introspector.getBeanInfo(c);
                    PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
                    if (descriptors == null || descriptors.length == 0) {
                        LOGGER.warn("no property descriptors");
                        return;
                    }
                    ViewMode newValue = (ViewMode) evt.getNewValue();
                    for (PropertyDescriptor p : descriptors) {
                        try {
                            Method setter = p.getWriteMethod();
                            if (setter == null) {
                                LOGGER.debug("write method not found for property: " + p.getName());
                                continue;
                            }
                            ReadOnlyParticipant r = setter.getAnnotation(ReadOnlyParticipant.class);
                            if (r != null) {
                                boolean value = ViewMode.DEFAULT.equals(newValue);
                                if (r.negate()) {
                                    value = !value;
                                }
                                setter.invoke(DualModePresentationModel.this, value);
                                LOGGER.info("ReadOnlyParticipant " + p.getName() + " set to " + value);
                            }
                        } catch (IllegalArgumentException e) {
                            LOGGER.error("cannot set value for ReadOnlyParticipant: " + p.getName(), e);
                        } catch (IllegalAccessException e) {
                            LOGGER.error("cannot set value for ReadOnlyParticipant: " + p.getName(), e);
                        } catch (InvocationTargetException e) {
                            LOGGER.error("cannot set value for ReadOnlyParticipant: " + p.getName(), e);
                        }
                    }
                } catch (IntrospectionException e) {
                    LOGGER.error("error changing mode:", e);
                }
            }
        }
    }
}
