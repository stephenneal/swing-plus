package com.swingplus.bind;

import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Converter;

import com.google.common.collect.Lists;

/**
 * Simple API for binding operations. Binding operations are used to bind view components values, state etc to its
 * model.
 * <p>
 * {@link Binder} provides the public binding API and operations to manage the bindings. Under the covers is Better
 * Beans Binding, {@link http://kenai.com/projects/betterbeansbinding/pages/Home). Better Beans Binding is an
 * implementation born from JSR-295 - Beans Binding, {@link http://jcp.org/en/jsr/detail?id=295}.
 * </p>
 * <h2>Managing bindings</h2>
 * <p>
 * This class ensures bindings are correctly managed. The bindings must be managed so that they can be disposed of when
 * the view is disposed. Not disposing the bindings can result in a memory leak.
 * </p>
 * 
 * @author Stephen Neal
 * 
 * @param B bean (aka model) type
 */
public class Binder<B> {

    private B bean;

    private List<Binding<?, ?, ?, ?>> bindings = Lists.newArrayList();
    private boolean released = false;

    public Binder(B bean) {
        super();
        if (bean == null) {
            throw new IllegalArgumentException("bean is null");
        }
        this.bean = bean;
    }

    /**
     * Get the underlying bean.
     * 
     * @return the underlying bean
     */
    public B getBean() {
        return this.bean;
    }

    public void release() {
        synchronized (this.bindings) {
            if (this.released) {
                return;
            }
            for (Binding<?, ?, ?, ?> b : this.bindings) {
                b.unbind();
            }
            this.bindings.clear();
            this.bindings = null;
            this.released = true;
        }
    }

    // BindingSupport implementation
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Bind the "text" property of a text component to a field (of type {@link String} on the underlying bean. The
     * initial value is taken from the bean.
     * 
     * @param component text component
     * @param fieldName name of the field on the underlying bean
     */
    public void bindText(JTextComponent component, String fieldName) {
        bindAndRegister(TextBindings.text(this.bean, String.class, fieldName, component));
    }

    /**
     * Bind the "text" property of a text component to a field (of type {@link Double}) on the underlying bean. The
     * initial value is taken from the bean.
     * 
     * @param component text component
     * @param fieldName name of the field on the underlying bean
     */
    public <T> void bindText(JTextComponent component, String fieldName, Class<T> fieldClass) {
        Binding<B, T, JTextComponent, String> binding = TextBindings.text(this.bean, fieldClass, fieldName, component);
        binding.setConverter(converter(fieldClass, String.class));
        bindAndRegister(binding);
    }

    // /**
    // * Bind the "calendar" property of a {@link JCalendar} to a field (of type {@code Calendar}) on the underlying
    // bean.
    // * The initial value is taken from the bean.
    // *
    // * @param component date chooser
    // * @param fieldName name of the field on the underlying bean, the field must be a {@code Date}
    // */
    // public void bindCalendar(final JCalendar component, String fieldName) {
    // bindAndRegister(DateBindings.calendar(this.bean, fieldName, component));
    // }
    //
    // /**
    // * Bind the "date" property of a {@link JDateChooser} to a field (of type {@code Date}) on the underlying bean.
    // The
    // * initial value is taken from the bean.
    // *
    // * @param component date chooser
    // * @param fieldName name of the field on the underlying bean, the field must be a {@code Date}
    // */
    // public void bindDate(JDateChooser component, String fieldName) {
    // bindAndRegister(DateBindings.date(this.bean, fieldName, component));
    // }

    /**
     * Bind the "model" property of a {@link JComboBox} to a field (of type {@link ListModel}) on the underlying bean.
     * The initial selection is taken from the bean. Selection is bound to the field matching {@code fieldName}, list
     * content is bound to the field matching {@code fieldName + "List"}.
     * 
     * @param component component
     * @param fieldName name used for binding
     */
    public void bindJComboBox(JComboBox component, String fieldName) {
        bindAndRegister(ListBindings.model(this.bean, fieldName + "List", component));
        bindAndRegister(ListBindings.selection(this.bean, fieldName, component));
    }

    /**
     * Bind the "model" property of a {@link JTable} to a field (of type {@link ListModel}) on the underlying bean. The
     * initial selection is taken from the bean. Selection is bound to the field matching {@code fieldName + "Selected"}
     * , list content is bound to the field matching {@code fieldName}.
     * 
     * @param component component
     * @param fieldName name used for binding
     * @param columnMap map of the bean field names (keys) to column names (values)
     */
    public void bindJTable(JTable component, String fieldName, Map<String, String> columnMap) {
        bindAndRegister(ListBindings.model(this.bean, fieldName, component, columnMap));
        bindAndRegister(ListBindings.selection(this.bean, fieldName + "Selected", component));
    }

    // Private
    // -----------------------------------------------------------------------------------------------------------------

    private void bindAndRegister(Binding<?, ?, ?, ?> binding) {
        binding.bind();
        this.bindings.add(binding);
    }

    @SuppressWarnings("unchecked")
    private static <S, T> Converter<S, T> converter(Class<S> source, Class<T> target) {
        if (source.equals(target)) {
            // Converter not required!
            return null;
        }
        if (target == String.class) {
            if (source == Double.class) {
                return (Converter<S, T>) BindConverters.DOUBLE_TO_STRING_CONVERTER;
            }
        }
        throw new RuntimeException("conversion not supported, implement the converter required");
    }
}
