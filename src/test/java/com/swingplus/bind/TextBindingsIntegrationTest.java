package com.swingplus.bind;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Binding;
import org.junit.Test;

/**
 * Integration test for {@link TextBindings}.
 * 
 * @author steve
 * @since 18/07/2011
 */
public class TextBindingsIntegrationTest {

    /**
     * Test for {@link TextBindings#text(Object, Class, String, JTextComponent)}. Verifies binding updates correctly in
     * both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testText() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JTextField textField = new JTextField();

        // Bind
        Binding<TestBean, String, JTextComponent, String> binding = TextBindings.text(bean, String.class, "string",
                        textField);
        binding.bind();

        // Test
        assertEquals(null, bean.getString());
        assertEquals("", textField.getText());

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

}
