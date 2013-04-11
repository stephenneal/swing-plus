package com.swingplus.bind.bbb;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateListener;

/**
 * Swing property wraps {@link Property} to ensure values for Swing component properties are set in the EDT.
 * 
 * @author Stephen Neal
 * @since 21/04/2011
 * 
 * @param <S> the type of source object that this {@code SwingProperty} operates on
 * @param <V> the type of value that this {@code SwingProperty} represents
 */
class SwingProperty<S, V> extends Property<S, V> {

    /**
     * Creates an instance of {@code SwingProperty} for the given path.
     * 
     * @param path the path
     * @return an instance of {@code SwingProperty} for the given path
     * @throws IllegalArgumentException if the path is null, or contains no property names
     */
    public static final <S, V> SwingProperty<S, V> create(String path) {
        return new SwingProperty<S, V>(null, path, null);
    }

    /**
     * Creates an instance of {@code SwingProperty} for the given path.
     * 
     * @param path the path
     * @return an instance of {@code SwingProperty} for the given path
     * @throws IllegalArgumentException if the path is null, or contains no property names
     */
    public static final <S, V> SwingProperty<S, V> create(String path, Class<?> targetType) {
        return new SwingProperty<S, V>(null, path, targetType);
    }

    /**
     * Creates an instance of {@code SwingProperty} for the given base property and path. The path is relative to the
     * value of the base property.
     * 
     * @param baseProperty the base property
     * @param path the path
     * @return an instance of {@code SwingProperty} for the given base property and path
     * @throws IllegalArgumentException if the path is null, or contains no property names
     */
    public static final <S, V> SwingProperty<S, V> create(Property<S, ?> baseProperty, String path) {
        return new SwingProperty<S, V>(baseProperty, path, null);
    }

    private BeanProperty<S, V> beanProperty;
    private String path;
    private Class<?> targetType;

    /**
     * @throws IllegalArgumentException for empty or {@code null} path.
     */
    private SwingProperty(Property<S, ?> baseProperty, String path, Class<?> targetType) {
        this.beanProperty = BeanProperty.create(baseProperty, path);
        this.path = path;
        this.targetType = targetType;
    }

    @Override
    public final void addPropertyStateListener(S source, PropertyStateListener listener) {
        this.beanProperty.addPropertyStateListener(source, listener);
    }

    @Override
    public final void removePropertyStateListener(S source, PropertyStateListener listener) {
        this.beanProperty.removePropertyStateListener(source, listener);
    }

    @Override
    public final PropertyStateListener[] getPropertyStateListeners(S source) {
        return this.beanProperty.getPropertyStateListeners(source);
    }

    public final boolean isListening(S source) {
        return this.beanProperty.isListening(source);
    }

    @Override
    public Class<? extends V> getWriteType(S source) {
        return this.beanProperty.getWriteType(source);
    }

    @Override
    public V getValue(S source) {
        V value = this.beanProperty.getValue(source);
        // Special handling for binding a "text" property of a Swing component to a non-String field.
        // If the target property is a String return the value as is, otherwise return null instead of empty string. The
        // BetterBeansBinding default conversion (see org.jdesktop.beansbinding.Converter) for non-String types fails
        // for an empty string (parsers cannot convert it to something meaningful) and therefore the target value is not
        // updated (it retains any existing value which does not necessarily reflect the fact the component no longer
        // has a value. By returning null in place of an empty string here the conversion is ignored and the target
        // value is set to null.
        //
        // NB. it is probably preferable that this logic be in the conversion. Unfortunately the default conversion
        // provided by BetterBeansBinding cannot be re-used (cannot be accessed outside of the package). That would mean
        // have to write our own version of all those converters plus include handling for an empty string (either by
        // wrapping the converters in some way or putting it into each converter) and a converter applied to every
        // JTextComponent binding. For now this is deemed a much simpler solution.
        if ("text".equals(this.path) && source instanceof JComponent && !String.class.equals(this.targetType)
                        && "".equals(value)) {
            return null;
        }
        return value;
    }

    @Override
    public void setValue(final S source, final V value) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingProperty.this.beanProperty.setValue(source, value);
            }
        });
    }

    @Override
    public boolean isReadable(S source) {
        return this.beanProperty.isReadable(source);
    }

    @Override
    public boolean isWriteable(S source) {
        return this.beanProperty.isWriteable(source);
    }

    @Override
    public String toString() {
        return this.beanProperty.toString();
    }

    @Override
    public int hashCode() {
        return this.beanProperty.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.beanProperty.equals(obj);
    }

}
