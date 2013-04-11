package com.swingplus.bind.bbb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Property;

/**
 * Encapsulate operations that bind the value of a "text" property on a {@link JComponent} to a field.
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
     * Create a binding of the bean field to the "text" property of a {@link JComponent}, i.e {@link JTextComponent},
     * {@link JLabel}. Refer to {@link #createBinding(Object, String, JComponent)} for more information about the
     * binding.
     * 
     * @param bean
     * @param fieldName name of the bean field to bind
     * @param component {@link JComponent} to bind (must have a "text" property)
     * @return binding instance
     */
    static <B, V> Binding<B, V, JComponent, String> text(B bean, String fieldName, JComponent component) {
        Binding<B, V, JComponent, String> binding = createBinding(bean, fieldName, component);
        setConverter(bean, binding);
        return binding;
    }

    /**
     * Create a binding of the bean field to the "text" property of a {@link JComponent}, i.e {@link JTextComponent},
     * {@link JLabel} and specify the {@link DateFormat} to use for converting {@link Date} to/from {@link String}.
     * Refer to {@link #createBinding(Object, String, JComponent)} for more information about the binding.
     * 
     * @param bean
     * @param fieldName name of the bean field to bind
     * @param component {@link JComponent} to bind (must have a "text" property)
     * @param format date format to convert {@link Date} to/from {@link String}
     * @return binding instance
     */
    static <B> Binding<B, Date, JComponent, String> text(B bean, String fieldName, JComponent component,
                    DateFormat format) {
        Binding<B, Date, JComponent, String> binding = createBinding(bean, fieldName, component);
        setDateConverter(bean, binding, format);
        return binding;
    }

    // Set a converter (if required). Often the default conversions provided by BetterBeansBinding are sufficient.
    @SuppressWarnings("unchecked")
    private static <B, V> void setConverter(B bean, Binding<B, V, JComponent, String> binding) {
        Class<?> writeType = binding.getSourceProperty().getWriteType(bean);
        if (Date.class.isAssignableFrom(writeType)) {
            setDateConverter(bean, (Binding<B, Date, JComponent, String>) binding, new SimpleDateFormat());
        }
    }

    // Set a converter (if required). Often the default conversions provided by BetterBeansBinding are sufficient.
    private static <B> void setDateConverter(B bean, Binding<B, Date, JComponent, String> binding, DateFormat format) {
        Class<?> writeType = binding.getSourceProperty().getWriteType(bean);
        if (Date.class.isAssignableFrom(writeType)) {
            binding.setConverter(Converter2.newDateString(format));
        }
    }

    /**
     * Create a binding of the bean field to the "text" property of a {@link JComponent}, i.e {@link JTextComponent},
     * {@link JLabel}. This will fail if the {@link JComponent} does not have a "text" property. The text component
     * value is updated to match the value of the field. The field type does not have to be a {@link String}, for types
     * other than {@link String} a converter is applied. BetterBeansBinding provides plenty of default conversions which
     * are hooked into the {@link Binding}, where those are not sufficient custom conversions are added.
     * <p>
     * The {@link UpdateStrategy} for a {@link JLabel} is {@link UpdateStrategy#READ}, for any other component it is
     * {@link UpdateStrategy.READ_WRITE}. A {@link JLabel} is {@link UpdateStrategy.READ} because it does not accept
     * user input and therefore cannot have its value set except via code i.e invoking {@link JLabel#setText(String)}.
     * When binding is being used for a label this should never be the case, the value should always be updated via the
     * field it is bound to. In the future an API with {@link UpdateStrategy} can be added if required.
     * </p>
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param bean
     * @param fieldName name of the bean field to bind
     * @param component {@link JComponent} to bind (must have a "text" property)
     * @return the {@link Binding}
     */
    private static <B, V> Binding<B, V, JComponent, String>
                    createBinding(B bean, String fieldName, JComponent component) {
        Property<B, V> bP = BeanProperty.create(fieldName);
        Property<JComponent, String> cP = SwingProperty.create("text", bP.getWriteType(bean));
        UpdateStrategy us = UpdateStrategy.READ_WRITE;
        if (component instanceof JLabel) {
            us = UpdateStrategy.READ;
        }
        return Bindings.createAutoBinding(us, bean, bP, component, cP);
    }

}
