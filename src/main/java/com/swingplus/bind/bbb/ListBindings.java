package com.swingplus.bind.bbb;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JTable;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Converter;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingbinding.adapters.JComboBoxAdapterProvider;
import org.jdesktop.swingbinding.adapters.JTableAdapterProvider;

/**
 * Encapsulate operations that bind the content and selection properties of a component. Components supported are
 * {@link JComboBox} and {@link JTable}.
 * <p>
 * NB. this is not intended to be used directly except by {@code Binder}. {@link Binder} provides the public binding API
 * and operations to manage the bindings.
 * </p>
 * 
 * @author Stephen Neal
 * @since 18/04/2011
 */
class ListBindings {

    /**
     * Create a binding of the bean field to the {@link JComboBox}. Refer to
     * {@link SwingBindings#createJComboBoxBinding(UpdateStrategy, Object, org.jdesktop.beansbinding.Property, JComboBox)}
     * .
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param <E> the type of elements in the source {@code List}
     * @param <B> the type of source object (on which the source property resolves to {@code List})
     * @param bean bean
     * @param elementClass type of elements in the list
     * @param fieldName name of the field on the bean
     * @param component component
     * @return binding instance
     */
    static <B, E> JComboBoxBinding<E, B, JComboBox> model(B bean, String fieldName, JComboBox component) {
        BeanProperty<B, List<E>> bP = BeanProperty.create(fieldName);
        return SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, bean, bP, component);
    }

    /**
     * Create a binding of the bean field to the {@link JComboBox} selection. The component is updated with the value
     * from the bean field.
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param bean bean
     * @param elementClass type of elements in the list
     * @param fieldName name of the field on the bean
     * @param component date chooser component
     * @return binding instance
     */
    static <B, E> Binding<B, E, Object, E> selection(B bean, String fieldName, JComboBox component) {
        Property<Object, E> cP = SwingProperty.create("selectedItem");
        JComboBoxAdapterProvider adapterProvider = new JComboBoxAdapterProvider();
        Object adapter = adapterProvider.createAdapter(component, "selectedItem");
        Property<B, E> bP = BeanProperty.create(fieldName);
        return Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean, bP, adapter, cP);
    }

    /**
     * Create a binding of the bean field to the {@link JTable}. The binding ensures the table is not editable.
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param <E> the type of elements in the source {@code List}
     * @param <B> the type of source object (on which the source property resolves to {@code List})
     * @param bean bean
     * @param fieldName name of the field on the bean
     * @param component table component
     * @param columnMap map of the bean field names (keys) to column names (values)
     * @return binding instance
     */
    static <B, E> JTableBinding<E, B, JTable> model(B bean, String fieldName, JTable component,
                    Map<String, String> columnMap) {
        BeanProperty<B, List<E>> bP = BeanProperty.create(fieldName);
        JTableBinding<E, B, JTable> binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, bean, bP,
                        component);
        binding.setEditable(false);
        if (columnMap != null) {
            for (Entry<String, String> e : columnMap.entrySet()) {
                BeanProperty<E, Object> create = BeanProperty.create(e.getKey());
                binding.addColumnBinding(create).setColumnName(e.getValue());
            }
        }
        return binding;
    }

    /**
     * Create a binding of the bean field to the {@link JTable} selection. The bean is updated with the value from the
     * table.
     * <p>
     * NB. creates the binding but does not actually bind. It is a helper method intended for use in the {@code Binder}
     * which ensures bindings are properly managed.
     * </p>
     * 
     * @param <E> the type of elements in the source {@code List}
     * @param <B> the type of source object (on which the source property resolves to {@code List})
     * @param bean bean
     * @param elementClass type of elements in the list
     * @param fieldName name of the field on the bean
     * @param component table component
     * @return binding instance
     */
    static <B, E> Binding<Object, List<E>, B, List<E>> selection(B bean, String fieldName, JTable component) {
        String tableSelectionFieldName = "selectedElements_IGNORE_ADJUSTING";
        Property<Object, List<E>> cP = BeanProperty.create(tableSelectionFieldName);
        Object adapter = new JTableAdapterProvider().createAdapter(component, tableSelectionFieldName);
        Property<B, List<E>> bP = BeanProperty.create(fieldName);
        // Must make the table the source since it only supports read
        AutoBinding<Object, List<E>, B, List<E>> binding = Bindings.createAutoBinding(UpdateStrategy.READ, adapter, cP,
                        bean, bP);
        // Provide a converter so that the bean field can be an ObservableList.
        binding.setConverter(new Converter<List<E>, List<E>>() {
            @Override
            public List<E> convertForward(List<E> value) {
                if (value instanceof ObservableList) {
                    return value;
                }
                return ObservableCollections.observableList(value);
            }

            @Override
            public List<E> convertReverse(List<E> value) {
                return value;
            }
        });
        return binding;
    }

}
