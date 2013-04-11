package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingbinding.SwingBindings;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.swingplus.bind.TestBean;

/**
 * Tests for {@link Binder}.
 * <p>
 * This is considered and integration test because it does not test {@link AbstractBinder} in isolation, it tests real
 * bindings (which are provided by BetterBeans Binding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 10/04/2013
 */
public class AbstractBinderIntegrationTest {

    private AbstractBinder<TestBean> binder;

    @Before
    public void setUp() {
        TestBean bean = TestBean.newInstance();
        this.binder = new AbstractBinder<TestBean>(bean) {
        };
    }

    /**
     * Test {@link AbstractBinder#AbstractBinder(Object)}.
     */
    @Test
    public void testConstructorBean() {
        try {
            new AbstractBinder<TestBean>(null) {
            };
            fail("exception expected, bean is null");
        } catch (Exception e) {
            // expected
        }

        TestBean bean = TestBean.newInstance();
        AbstractBinder<TestBean> binder = new AbstractBinder<TestBean>(bean) {
        };
        assertEquals(bean, binder.getBean());
    }

    /**
     * Test {@link AbstractBinder#getBean()}.
     */
    @Test
    public void testGetBean() {
        TestBean bean = TestBean.newInstance();
        AbstractBinder<TestBean> binder = new AbstractBinder<TestBean>(bean) {
        };
        assertEquals(bean, binder.getBean());
    }

    /**
     * Test {@link AbstractBinder#release()}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testRelease() {
        Field bindingsField = ReflectionUtils.findField(AbstractBinder.class, "bindings");
        ReflectionUtils.makeAccessible(bindingsField);
        List<Binding<?, ?, ?, ?>> localBindings = new ArrayList<Binding<?, ?, ?, ?>>();
        List<Binding<?, ?, ?, ?>> bindings = (List<Binding<?, ?, ?, ?>>) ReflectionUtils.getField(bindingsField,
                        this.binder);

        int bindingCount = 0;
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, this.binder.getBean(),
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        binding.bind();
        bindings.add(binding);
        localBindings.add(binding);
        bindingCount++;

        BeanProperty<TestBean, List<String>> bP = BeanProperty.create("stringList");
        binding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, this.binder.getBean(), bP,
                        new JComboBox());
        binding.bind();
        bindings.add(binding);
        localBindings.add(binding);
        bindingCount++;

        bindings = (List<Binding<?, ?, ?, ?>>) ReflectionUtils.getField(bindingsField, this.binder);
        assertEquals(bindingCount, bindings.size());
        for (Binding<?, ?, ?, ?> b : bindings) {
            assertTrue(b.isBound());
        }
        for (Binding<?, ?, ?, ?> b : localBindings) {
            assertTrue(b.isBound());
        }

        Field releasedField = ReflectionUtils.findField(AbstractBinder.class, "released");
        ReflectionUtils.makeAccessible(releasedField);
        Boolean released = (Boolean) ReflectionUtils.getField(releasedField, this.binder);
        assertFalse(released);

        this.binder.release();

        // Assert released flag
        released = (Boolean) ReflectionUtils.getField(releasedField, this.binder);
        assertTrue(released);

        // Assert bindings
        for (Binding<?, ?, ?, ?> b : localBindings) {
            assertFalse(b.isBound());
        }

        // Assert binding list cleared
        bindings = (List<Binding<?, ?, ?, ?>>) ReflectionUtils.getField(bindingsField, this.binder);
        assertEquals(null, bindings);

        // Assert bean detached
        assertEquals(null, this.binder.getBean());
    }
}
