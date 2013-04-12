/**
 * Copyright (C) 2011 Stephen Neal
 */
package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingbinding.SwingBindings;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.swingplus.bind.TestBean;

/**
 * Tests the functionality of {@link BindingService}.
 * <p>
 * This does not test the class in isolation (as per a unit test), it tests with real bindings (BetterBeansBinding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 11/04/2013
 */
public class BindingServiceFunctionalTest {

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#BindingManager()}.
     */
    @Test
    public void testBindingManager() {
        BindingService manager = new BindingService();
        Field bindingsField = ReflectionUtils.findField(BindingService.class, "bindingMap");
        ReflectionUtils.makeAccessible(bindingsField);
        @SuppressWarnings("unchecked")
        Map<Object, List<Binding<?, ?, ?, ?>>> managerMap = (Map<Object, List<Binding<?, ?, ?, ?>>>) ReflectionUtils
                        .getField(bindingsField, manager);
        assertNotNull(managerMap);
        Field releasedField = ReflectionUtils.findField(BindingService.class, "released");
        ReflectionUtils.makeAccessible(releasedField);
        Boolean released = (Boolean) ReflectionUtils.getField(releasedField, manager);
        assertFalse(released);
    }

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#release()}.
     */
    @Test
    public void testRelease() {
        BindingService manager = new BindingService();
        TestBean bean = TestBean.newInstance();
        Field bindingsField = ReflectionUtils.findField(BindingService.class, "bindingMap");
        ReflectionUtils.makeAccessible(bindingsField);
        @SuppressWarnings("unchecked")
        Map<Object, List<Binding<?, ?, ?, ?>>> managerMap = (Map<Object, List<Binding<?, ?, ?, ?>>>) ReflectionUtils
                        .getField(bindingsField, manager);
        List<Binding<?, ?, ?, ?>> localList = new ArrayList<Binding<?, ?, ?, ?>>();
        List<Binding<?, ?, ?, ?>> managerList = new ArrayList<Binding<?, ?, ?, ?>>();

        int bindingCount = 0;
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean,
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        binding.bind();
        managerList.add(binding);
        managerMap.put(bean, managerList);
        localList.add(binding);
        bindingCount++;

        BeanProperty<TestBean, List<String>> bP = BeanProperty.create("stringList");
        binding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, bean, bP, new JComboBox());
        binding.bind();
        managerList.add(binding);
        managerMap.put(bean, managerList);
        localList.add(binding);
        bindingCount++;

        managerList = managerMap.get(bean);
        assertNotNull(managerList);
        assertEquals(bindingCount, managerList.size());
        for (Binding<?, ?, ?, ?> b : managerList) {
            assertTrue(b.isBound());
        }
        for (Binding<?, ?, ?, ?> b : localList) {
            assertTrue(b.isBound());
        }

        Field releasedField = ReflectionUtils.findField(BindingService.class, "released");
        ReflectionUtils.makeAccessible(releasedField);
        Boolean released = (Boolean) ReflectionUtils.getField(releasedField, manager);
        assertFalse(released);

        manager.release();

        // Assert released flag
        released = (Boolean) ReflectionUtils.getField(releasedField, manager);
        assertTrue(released);

        // Assert bindings released (do not use the manager list instance, it will be empty giving a false positive)
        for (Binding<?, ?, ?, ?> b : localList) {
            assertFalse(b.isBound());
        }

        // Assert binding map cleared
        assertTrue(managerMap.isEmpty());
    }

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#release(java.lang.Object)}.
     */
    @Test
    public void testReleaseObject() {
        BindingService manager = new BindingService();
        TestBean bean1 = TestBean.newInstance();
        TestBean bean2 = TestBean.newInstance();

        Field bindingsField = ReflectionUtils.findField(BindingService.class, "bindingMap");
        ReflectionUtils.makeAccessible(bindingsField);
        @SuppressWarnings("unchecked")
        Map<Object, List<Binding<?, ?, ?, ?>>> managerMap = (Map<Object, List<Binding<?, ?, ?, ?>>>) ReflectionUtils
                        .getField(bindingsField, manager);
        List<Binding<?, ?, ?, ?>> localList = new ArrayList<Binding<?, ?, ?, ?>>();
        List<Binding<?, ?, ?, ?>> managerListBean1 = new ArrayList<Binding<?, ?, ?, ?>>();
        List<Binding<?, ?, ?, ?>> managerListBean2 = new ArrayList<Binding<?, ?, ?, ?>>();

        // Create bindings for bean 1
        int bindingCount = 0;
        int bean1BindingCount = 0;
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean1,
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        binding.bind();
        managerListBean1.add(binding);
        localList.add(binding);
        bindingCount++;
        bean1BindingCount++;

        BeanProperty<TestBean, List<String>> bP = BeanProperty.create("stringList");
        binding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, bean1, bP, new JComboBox());
        binding.bind();
        managerListBean1.add(binding);
        localList.add(binding);
        bindingCount++;
        bean1BindingCount++;

        managerMap.put(bean1, managerListBean1);

        // Create bindings for bean 2
        binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean2, BeanProperty.create("string"),
                        new JTextField(), BeanProperty.create("text"));
        binding.bind();
        managerListBean2.add(binding);
        localList.add(binding);
        bindingCount++;

        bP = BeanProperty.create("stringList");
        binding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, bean2, bP, new JComboBox());
        binding.bind();
        managerListBean2.add(binding);
        localList.add(binding);
        bindingCount++;

        managerMap.put(bean2, managerListBean2);

        managerListBean1 = managerMap.get(bean1);
        for (Binding<?, ?, ?, ?> b : managerListBean1) {
            assertTrue(b.isBound());
        }
        for (Binding<?, ?, ?, ?> b : localList) {
            assertTrue(b.isBound());
        }

        Field releasedField = ReflectionUtils.findField(BindingService.class, "released");
        ReflectionUtils.makeAccessible(releasedField);
        Boolean released = (Boolean) ReflectionUtils.getField(releasedField, manager);
        assertFalse(released);

        manager.release(bean1);

        // Assert released flag (manager should not be released)
        released = (Boolean) ReflectionUtils.getField(releasedField, manager);
        assertFalse(released);

        // Assert 1 bean is registered (first bean should have been removed
        assertEquals(1, managerMap.size());
        assertFalse(managerMap.containsKey(bean1));
        assertTrue(managerMap.containsKey(bean2));
        int remainingCount = 0;
        for (Entry<Object, List<Binding<?, ?, ?, ?>>> e : managerMap.entrySet()) {
            remainingCount += e.getValue().size();
        }
        assertEquals(bindingCount - bean1BindingCount, remainingCount);
        assertTrue(managerListBean1.isEmpty());

        // Assert bindings released (do not use the manager list instance, it will be empty giving a false positive)
        for (Binding<?, ?, ?, ?> b : localList) {
            boolean bound = !b.getSourceObject().equals(bean1);
            assertEquals(bound, b.isBound());
        }

        // Release (this does not form part of the test it is here for good practice)
        manager.release();
    }

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#bind(org.jdesktop.beansbinding.Binding)}.
     */
    @Test
    public void testBind() {
        TestBean bean = TestBean.newInstance();
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean,
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        BindingService manager = new BindingService();
        manager.bind(binding);
        assertTrue(binding.isBound());

        Field bindingsField = ReflectionUtils.findField(BindingService.class, "bindingMap");
        ReflectionUtils.makeAccessible(bindingsField);
        @SuppressWarnings("unchecked")
        Map<Object, List<Binding<?, ?, ?, ?>>> managerMap = (Map<Object, List<Binding<?, ?, ?, ?>>>) ReflectionUtils
                        .getField(bindingsField, manager);
        List<Binding<?, ?, ?, ?>> managerList = managerMap.get(bean);

        assertTrue(managerList.contains(binding));

        // Release (this does not form part of the test it is here for good practice)
        manager.release();
    }

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#release(java.util.List)}.
     */
    @Test
    public void testReleaseBindingList() {
        TestBean bean = TestBean.newInstance();
        List<Binding<?, ?, ?, ?>> bindings = new ArrayList<Binding<?, ?, ?, ?>>();

        int bindingCount = 0;
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean,
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        binding.bind();
        binding.addBindingListener(new AbstractBindingListener() {
        });
        bindings.add(binding);
        bindingCount++;

        BeanProperty<TestBean, List<String>> bP = BeanProperty.create("stringList");
        binding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ_WRITE, bean, bP, new JComboBox());
        binding.bind();
        binding.addBindingListener(new AbstractBindingListener() {
        });
        bindings.add(binding);
        bindingCount++;

        assertEquals(bindingCount, bindings.size());
        for (Binding<?, ?, ?, ?> b : bindings) {
            assertTrue(b.isBound());
            assertEquals(1, b.getBindingListeners().length);
        }

        BindingService.release(bindings);

        // Assert bindings not bound
        for (Binding<?, ?, ?, ?> b : bindings) {
            assertFalse(b.isBound());
            assertEquals(0, b.getBindingListeners().length);
        }

        // Assert bindings removed from the list
        assertTrue(bindings.isEmpty());
    }

    /**
     * Test method for {@link com.swingplus.bind.bbb.BindingService#release(org.jdesktop.beansbinding.Binding)}.
     */
    @Test
    public void testReleaseBinding() {
        TestBean bean = TestBean.newInstance();
        Binding<?, ?, ?, ?> binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, bean,
                        BeanProperty.create("string"), new JTextField(), BeanProperty.create("text"));
        binding.bind();
        assertTrue(binding.isBound());
        binding.addBindingListener(new AbstractBindingListener() {
        });
        assertEquals(1, binding.getBindingListeners().length);
        BindingService.release(binding);

        // Assert binding not bound
        assertFalse(binding.isBound());
        assertEquals(0, binding.getBindingListeners().length);
    }

}
