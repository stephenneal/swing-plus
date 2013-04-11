package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beansbinding.Binding;
import org.junit.Test;

import com.swingplus.bind.TestBean;

/**
 * Tests the functionality of {@link TextBindings}.
 * <p>
 * This does not test the class in isolation (as per a unit test), it tests with real bindings (BetterBeansBinding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 18/07/2011
 */
public class TextBindingsFunctionalTest {

    /**
     * Test {@link TextBindings#text(Object, String, JComponent)} for a {@link JTextField} and property of type
     * {@link String}.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testTextJTextFieldString() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JTextField textField = new JTextField();

        // Bind
        Binding<TestBean, String, JComponent, String> binding = TextBindings.text(bean, "string", textField);
        binding.bind();

        // Test
        assertEquals(null, bean.getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });

        // Update the bean value
        final String value = "value";
        bean.setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, textField.getText());
            }
        });
        // Clear the text field
        textField.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", bean.getString());
            }
        });
        // Update the text field with a value
        textField.setText(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, bean.getString());
            }
        });
        // Clear the bean value
        bean.setString(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });
    }

    /**
     * Test {@link TextBindings#text(Object, String, JComponent)} for a {@link JTextField} and property of type
     * {@link Integer}.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testTextJTextFieldInteger() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JTextField textField = new JTextField();

        // Bind
        Binding<TestBean, String, JComponent, String> binding = TextBindings.text(bean, "integr", textField);
        binding.bind();

        // Test
        assertEquals(null, bean.getIntegr());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });

        // Update the bean value
        final Integer value = 10;
        bean.setIntegr(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value.toString(), textField.getText());
            }
        });
        // Clear the text field
        textField.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getIntegr());
            }
        });
        // Update the text field with a value
        textField.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, bean.getIntegr());
            }
        });
        // Clear the bean value
        bean.setIntegr(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });
    }

    /**
     * Test {@link TextBindings#text(Object, String, JComponent)} for a {@link JLabel} and property of type
     * {@link String}.
     */
    @Test
    public void testBindTextJLabelToString() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JLabel label = new JLabel();

        // Bind
        Binding<TestBean, String, JComponent, String> binding = TextBindings.text(bean, "string", label);
        binding.bind();

        // Test
        assertEquals(null, bean.getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });

        // Update the bean value
        final String value = "some value";
        bean.setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value.toString(), label.getText());
            }
        });
        // Set the label to null (binding is read only so bean should not be updated)
        label.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, bean.getString());
            }
        });
        // Set the same value on the bean property, the label will not be updated (same value means no property change)
        bean.setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Set a new value on the bean property
        final String newValue = "some new value";
        bean.setString(newValue);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(newValue.toString(), label.getText());
            }
        });
        // Set the bean value to null
        bean.setString(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Update the label with a value (binding is read only so bean should not be updated)
        label.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getString());
            }
        });
        // Set the label to an empty string, bean property should be set to null
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getString());
            }
        });
    }

    /**
     * Test {@link TextBindings#text(Object, String, JComponent)} for a {@link JLabel} and property of type
     * {@link Integer}.
     */
    @Test
    public void testBindTextJLabelToInteger() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JLabel label = new JLabel();

        // Bind
        Binding<TestBean, Integer, JComponent, String> binding = TextBindings.text(bean, "integr", label);
        binding.bind();

        // Test
        assertEquals(null, bean.getIntegr());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });

        // Update the bean value
        final Integer value = Integer.valueOf(2);
        bean.setIntegr(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value.toString(), label.getText());
            }
        });
        // Set the label to null (binding is read only so bean should not be updated)
        label.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, bean.getIntegr());
            }
        });
        // Set the same value on the bean property, the label will not be updated (same value means no property change)
        bean.setIntegr(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Set a new value on the bean property
        final Integer newValue = Integer.valueOf(10);
        bean.setIntegr(newValue);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(newValue.toString(), label.getText());
            }
        });
        // Set the bean value to null
        bean.setIntegr(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Update the label with a value (binding is read only so bean should not be updated)
        label.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getIntegr());
            }
        });
        // Set the label to an empty string, bean property should be set to null
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getIntegr());
            }
        });
    }
}
