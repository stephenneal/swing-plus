package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beansbinding.Binding;
import org.junit.Before;
import org.junit.Test;

import com.swingplus.bind.TestBean;

/**
 * Tests for {@link Binder}.
 * <p>
 * This is considered and integration test because it does not test {@link Binder} in isolation, it tests real bindings
 * (which are provided by BetterBeans Binding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 29/07/2011
 */
public class BinderIntegrationTest {

    private Binder<TestBean> binder;

    @Before
    public void setUp() {
        TestBean bean = TestBean.newInstance();
        this.binder = new Binder<TestBean>(bean);
    }

    /**
     * Test {@link Binder#Binder(Object)}.
     */
    @Test
    public void testConstructorBean() {
        try {
            new Binder<TestBean>(null);
            fail("exception expected, bean is null");
        } catch (Exception e) {
            // expected
        }

        TestBean bean = TestBean.newInstance();
        Binder<TestBean> binder = new Binder<TestBean>(bean);
        assertEquals(bean, binder.getBean());
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)} for a {@link JTextField} and property of
     * type {@link String}.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testBindTextJTextFieldToString() throws InterruptedException, InvocationTargetException {
        final JTextField textField = new JTextField();

        // Bind
        Binding<TestBean, String, JComponent, String> binding = TextBindings.text(this.binder.getBean(), "string",
                        textField);
        binding.bind();

        // Test
        assertEquals(null, this.binder.getBean().getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });

        // Update the bean value
        final String value = "value";
        this.binder.getBean().setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, textField.getText());
            }
        });
        // Set the text field to null
        textField.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Update the text field with a value
        textField.setText(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Clear the bean value
        this.binder.getBean().setString(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)} for a {@link JTextField} and property of
     * type {@link Integer}.
     */
    @Test
    public void testBindTextJTextFieldToInteger() throws InterruptedException, InvocationTargetException {
        final JTextField textField = new JTextField();

        // Bind
        this.binder.bindText(textField, "integr");

        // Test
        assertEquals(null, this.binder.getBean().getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });

        // Update the bean value
        final Integer value = Integer.valueOf(2);
        this.binder.getBean().setIntegr(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value.toString(), textField.getText());
            }
        });
        // Set the text field to null
        textField.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Update the text field with a value
        textField.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Clear the bean value
        this.binder.getBean().setIntegr(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)} for a {@link JTextField} and property of
     * type {@link Double}.
     */
    @Test
    public void testBindTextJTextFieldToDouble() throws InterruptedException, InvocationTargetException {
        final JTextField textField = new JTextField();

        // Bind
        this.binder.bindText(textField, "duble");

        // Test
        assertEquals(null, this.binder.getBean().getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });

        // Update the bean value
        final Double value = Double.valueOf(1.0);
        this.binder.getBean().setDuble(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value.toString(), textField.getText());
            }
        });
        // Set the text field to null
        textField.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getDuble());
            }
        });
        // Update the text field with a value
        textField.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getDuble());
            }
        });
        // Clear the bean value
        this.binder.getBean().setDuble(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals("", textField.getText());
            }
        });
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)} for a {@link JLabel} and property of type
     * {@link String}.
     */
    @Test
    public void testBindTextJLabelToString() throws InterruptedException, InvocationTargetException {
        final JLabel label = new JLabel();

        // Bind
        this.binder.bindText(label, "string");

        // Test
        assertEquals(null, this.binder.getBean().getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });

        // Update the bean value
        final String value = "some value";
        this.binder.getBean().setString(value);
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
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Set the same value on the bean property, the label will not be updated (same value means no property change)
        this.binder.getBean().setString(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Set a new value on the bean property
        final String newValue = "some new value";
        this.binder.getBean().setString(newValue);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(newValue.toString(), label.getText());
            }
        });
        // Set the bean value to null
        this.binder.getBean().setString(null);
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
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Set the label to an empty string, bean property should be set to null
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)} for a {@link JLabel} and property of type
     * {@link Integer}.
     */
    @Test
    public void testBindTextJLabelToInteger() throws InterruptedException, InvocationTargetException {
        final JLabel label = new JLabel();

        // Bind
        this.binder.bindText(label, "integr");

        // Test
        assertEquals(null, this.binder.getBean().getString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });

        // Update the bean value
        final Integer value = Integer.valueOf(2);
        this.binder.getBean().setIntegr(value);
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
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Set the same value on the bean property, the label will not be updated (same value means no property change)
        this.binder.getBean().setIntegr(value);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, label.getText());
            }
        });
        // Set a new value on the bean property
        final Integer newValue = Integer.valueOf(10);
        this.binder.getBean().setIntegr(newValue);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(newValue.toString(), label.getText());
            }
        });
        // Set the bean value to null
        this.binder.getBean().setIntegr(null);
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
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Set the label to an empty string, bean property should be set to null
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
    }

}
