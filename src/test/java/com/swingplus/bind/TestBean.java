package com.swingplus.bind;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jdesktop.observablecollections.ObservableList;

import com.swingplus.PropertyChangeSupport2;

public class TestBean {

    public static TestBean newInstance() {
        TestBean bean = new TestBean();
        bean.setPropertyChangeSupport(new PropertyChangeSupport2(bean));
        return bean;
    }

    private transient PropertyChangeSupport propertyChangeSupport;
    private Date date;
    private Double duble;
    private ObservableList<String> stringList;
    private String string;
    private ObservableList<TestBean> testBeans;
    private ObservableList<TestBean> testBeansSelected;

    private TestBean() {
        super();
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date newValue) {
        Date oldValue = this.date;
        this.date = newValue;
        getPropertyChangeSupport().firePropertyChange("date", oldValue, newValue);
    }

    public Double getDuble() {
        return this.duble;
    }

    public void setDuble(Double newValue) {
        Double oldValue = this.duble;
        this.duble = newValue;
        getPropertyChangeSupport().firePropertyChange("duble", oldValue, newValue);
    }

    public ObservableList<String> getStringList() {
        return this.stringList;
    }

    public void setStringList(ObservableList<String> newValue) {
        ObservableList<String> oldValue = this.stringList;
        this.stringList = newValue;
        getPropertyChangeSupport().firePropertyChange("stringList", oldValue, newValue);
    }

    public String getString() {
        return this.string;
    }

    public void setString(String newValue) {
        String oldValue = this.string;
        this.string = newValue;
        getPropertyChangeSupport().firePropertyChange("string", oldValue, newValue);
    }

    public ObservableList<TestBean> getTestBeans() {
        return this.testBeans;
    }

    public void setTestBeans(ObservableList<TestBean> newValue) {
        ObservableList<TestBean> oldValue = this.testBeans;
        this.testBeans = newValue;
        getPropertyChangeSupport().firePropertyChange("testBeans", oldValue, newValue);
    }

    public ObservableList<TestBean> getTestBeansSelected() {
        return this.testBeansSelected;
    }

    public void setTestBeansSelected(ObservableList<TestBean> newValue) {
        ObservableList<TestBean> oldValue = this.testBeansSelected;
        this.testBeansSelected = newValue;
        getPropertyChangeSupport().firePropertyChange("testBeansSelected", oldValue, newValue);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE, false);
    }

    // PropertyChangeSupport delegate methods
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
