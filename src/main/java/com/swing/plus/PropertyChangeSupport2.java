package com.swing.plus;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends {@link PropertyChangeSupport} to prevent firing of events whenever property values are both {@code null}.
 * 
 * @author Stephen Neal
 */
public class PropertyChangeSupport2 extends PropertyChangeSupport {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyChangeSupport2.class);

    public PropertyChangeSupport2(Object sourceBean) {
        super(sourceBean);
    }

    /**
     * Fire a property change event if oldValue is not equal to new value.
     */
    @Override
    public void firePropertyChange(PropertyChangeEvent evt) {
        if (!ObjectUtils.equals(evt.getOldValue(), evt.getNewValue())) {
            LOGGER.info("firePropertyChange: source = " + getSimpleClassName(evt.getSource()) + "; name = "
                            + evt.getPropertyName() + "; oldValue = " + evt.getOldValue() + "; newValue = "
                            + evt.getNewValue());
            super.firePropertyChange(evt);
        }
    }

    private static String getSimpleClassName(Object o) {
        return o == null ? null : o.getClass().getSimpleName();
    }
}
