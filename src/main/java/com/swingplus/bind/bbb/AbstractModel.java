package com.swingplus.bind.bbb;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.swingplus.PropertyChangeSupport2;

/**
 * Model classes to be used in binding can extend this to get {@link PropertyChangeSupport} for compatibility with
 * BetterBeansBinding. Implementing classes must be sure to fire {@link PropertyChangeEvent}'s in setter's.
 * 
 * @author Stephen Neal
 * @since 10/04/2013
 */
public class AbstractModel {

    public static AbstractModel newInstance() {
        AbstractModel bean = new AbstractModel();
        bean.setPropertyChangeSupport(new PropertyChangeSupport2(bean));
        return bean;
    }

    private transient PropertyChangeSupport propertyChangeSupport;

    protected AbstractModel() {
        super();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false);
    }

    // PropertyChangeSupport delegate methods required for compatibility with BetterBeansBinding
    // -----------------------------------------------------------------------------------------------------------------

    public PropertyChangeSupport getPropertyChangeSupport() {
        return this.propertyChangeSupport;
    }

    public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport) {
        this.propertyChangeSupport = propertyChangeSupport;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    public boolean hasListeners(String propertyName) {
        return this.propertyChangeSupport.hasListeners(propertyName);
    }

}
