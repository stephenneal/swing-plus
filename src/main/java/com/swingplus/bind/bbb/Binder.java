package com.swingplus.bind.bbb;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Binding;

/**
 * Simple API for binding operations. Binding operations are used to bind view components values, state etc to its
 * model.
 * <p>
 * {@link Binder} provides the public binding API and by extending {@link AbstractBinder} it provides operations to
 * manage the bindings.
 * </p>
 * <h2>Managing bindings</h2>
 * <p>
 * This class ensures bindings are correctly managed. The bindings must be managed so that they can be disposed of when
 * the view is disposed. Not disposing the bindings can result in a memory leak.
 * </p>
 * <p>
 * Under the covers is BetterBeansBinding, {@link http ://kenai.com/projects/betterbeansbinding/pages/Home).
 * BetterBeansBinding is an implementation born from JSR-295 - Beans Binding, {@link http
 * ://jcp.org/en/jsr/detail?id=295}.
 * </p>
 * 
 * @author Stephen Neal
 * 
 * @param B bean (aka model) type
 */
public class Binder<B> extends AbstractBinder<B> {

    /**
     * Constructor requiring a bean (model).
     * 
     * @param bean bean, cannot be {@code null}
     * @see AbstractBinder#AbstractBinder(Object)(Object)
     */
    public Binder(B bean) {
        super(bean);
    }

    // Binding API
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Bind the "text" property of a text component to a field (of type {@link String} on the underlying bean. The
     * initial value is taken from the bean.
     * <p>
     * NB. Bind does not have to be to a field of type {@link String}. Under the covers converters are used for other
     * types.
     * </p>
     * 
     * @param component text component
     * @param fieldName name of the field on the underlying bean
     */
    public void bindText(final JTextComponent component, String fieldName) {
        B bean = getBean();
        Binding<B, ?, JComponent, String> binding = TextBindings.text(bean, fieldName, component);
        bindAndManage(binding);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String text = component.getText();
                if (text != null && text.length() > 0) {
                    component.setCaretPosition(component.getText().length());
                }
            }
        });
    }

    /**
     * Bind the "text" property of a text component to a field (of type {@link String} on the underlying bean. The
     * initial value is taken from the bean.
     * <p>
     * NB. Bind does not have to be to a field of type {@link String}. Under the covers converters are used for other
     * types.
     * </p>
     * 
     * @param label label
     * @param fieldName name of the field on the underlying bean
     */
    public void bindText(JLabel label, String fieldName) {
        bindAndManage(TextBindings.text(getBean(), fieldName, label));
    }

    /**
     * Bind the "model" property of a {@link JComboBox} to a field (of type {@link ListModel}) on the underlying bean.
     * The initial selection is taken from the bean. Selection is bound to the field matching {@code fieldName}, list
     * content is bound to the field matching {@code fieldName + "List"}.
     * 
     * @param component component
     * @param fieldName name used for binding
     */
    public void bindJComboBox(JComboBox component, String fieldName) {
        bindAndManage(ListBindings.model(getBean(), fieldName + "List", component));
        bindAndManage(ListBindings.selection(getBean(), fieldName, component));
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
        bindAndManage(ListBindings.model(getBean(), fieldName, component, columnMap));
        bindAndManage(ListBindings.selection(getBean(), fieldName + "Selected", component));
    }

}
