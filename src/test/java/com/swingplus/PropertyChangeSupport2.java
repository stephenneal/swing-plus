package com.swingplus;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import org.apache.log4j.Logger;

import com.google.common.base.Objects;

/**
 * Enhancement to {@link PropertyChangeSupport} to prevent firing of events whenever properties are equal.
 * 
 * @author Stephen Neal
 */
public class PropertyChangeSupport2 extends PropertyChangeSupport {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(PropertyChangeSupport2.class);

	public PropertyChangeSupport2(Object sourceBean) {
		super(sourceBean);
	}

	/**
	 * Fire a property change event if oldValue is not equal to new value.
	 */
	@Override
	public void firePropertyChange(PropertyChangeEvent evt) {
		if (!Objects.equal(evt.getOldValue(), evt.getNewValue())) {
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
