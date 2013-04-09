package com.swingplus.bind;

import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;

/**
 * Encapsulate operations that bind the value of a text component to a field.
 * <p>
 * NB. this is not intended to be used directly except by {@code Binder}. {@link Binder} provides the public binding API
 * and operations to manage the bindings.
 * </p>
 * 
 * @author Stephen Neal
 * @since 18/04/2011
 */
class TextBindings {

    /**
     * Create a binding of the bean field to the {@link JTextComponent} "text" property. The text component is updated
     * with the value from the field. The field type does not have to be a {@link String}, but it is important to note
     * that when it is not, a converter must be set on the binding.
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param bean bean
     * @param fieldClass field type (field type does not have to be a {@link String}, but it is important to note that
     *            when it is not, a converter must be set on the binding)
     * @param fieldName name of the field on the bean
     * @param component Swing text component
     * @return binding instance
     */
    static <B, V> Binding<B, V, JTextComponent, String> text(B bean, Class<V> fieldClass, String fieldName,
                    JTextComponent component) {
        Property<B, V> bP = BeanProperty.create(fieldName);
        Property<JTextComponent, String> cP = SwingProperty.create("text");
        return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, component, cP);
    }

}
