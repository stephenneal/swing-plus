package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Binding;
import org.junit.Test;

import com.swingplus.bind.TestBean;

/**
 * Tests for {@link TextBindings}.
 * <p>
 * This is considered and integration test because it does not test {@link TextBindings} in isolation, it tests real
 * bindings (which are provided by BetterBeans Binding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 18/07/2011
 */
public class TextBindingsIntegrationTest {

    /**
     * Test for {@link TextBindings#text(Object, Class, String, JTextComponent)} bound to a {@link String}. Verifies
     * binding updates correctly in both directions.
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
     * Test for {@link TextBindings#text(Object, Class, String, JTextComponent)} bound to an {@link Integer}. Verifies
     * binding updates correctly in both directions.
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
     * Test for {@link TextBindings#text(Object, Class, String, JLabel)} bound to a {@link String}. Verifies binding
     * updates correctly.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testTextJLabelString() throws InterruptedException, InvocationTargetException {
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
        final String value = "value";
        bean.setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, label.getText());
            }
        });
        // Set the label text to null
        label.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getString());
            }
        });
        // Update the text field with a value
        label.setText(value);
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
                assertEquals(null, label.getText());
            }
        });
        // Set the label text to an empty String
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", bean.getString());
            }
        });
    }

    /**
     * Test for {@link TextBindings#text(Object, Class, String, JLabel)} bound to an {@link Integer}. Verifies binding
     * updates correctly.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testTextJLabelInteger() throws InterruptedException, InvocationTargetException {
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
        final String value = "value";
        bean.setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, label.getText());
            }
        });
        // Set the label text to null
        label.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getString());
            }
        });
        // Update the text field with a value
        label.setText(value);
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
                assertEquals(null, label.getText());
            }
        });
        // Set the label text to an empty String
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", bean.getString());
            }
        });
    }

}
