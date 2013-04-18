package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swingplus.bind.TestBean;

/**
 * Tests the functionality of {@link ListBindings}.
 * <p>
 * This does not test the class in isolation (as per a unit test), it tests with real bindings (BetterBeansBinding).
 * </p>
 * 
 * @author Stephen Neal
 * @since 29/07/2011
 */
public class ListBindingsFunctionalTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Test for {@link ListBindings#model(Object, String, javax.swing.JComboBox)}. Verifies binding in both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testModelComboBox() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JComboBox comboBox = new JComboBox();
        // The bean list must be set prior to binding for it to be bound to the list instance
        List<String> l = new ArrayList<String>();
        final String value1 = "value1";
        l.add(value1);
        final ObservableList<String> list = ObservableCollections.observableList(l);
        bean.setStringList(list);

        // Bind
        JComboBoxBinding<Object, TestBean, JComboBox> binding = ListBindings.model(bean, "stringList", comboBox);
        binding.bind();

        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, comboBox.getModel().getSize());
                assertEquals(value1, comboBox.getModel().getElementAt(0));
                assertEquals(value1, comboBox.getItemAt(0));
            }
        });
        final String value2 = "value2";
        bean.getStringList().add(value2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(2, comboBox.getModel().getSize());
                assertEquals(value1, comboBox.getModel().getElementAt(0));
                assertEquals(value1, comboBox.getItemAt(0));
                assertEquals(value2, comboBox.getModel().getElementAt(1));
                assertEquals(value2, comboBox.getItemAt(1));
            }
        });
        bean.getStringList().remove(value2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, comboBox.getModel().getSize());
                assertEquals(value1, comboBox.getModel().getElementAt(0));
                assertEquals(value1, comboBox.getItemAt(0));
            }
        });

        // Changing the combo box model will not update the bean! Do this test last.
        comboBox.setModel(new DefaultComboBoxModel(new String[] { value1 }));
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, bean.getStringList().size());
                assertEquals(value1, bean.getStringList().get(0));
            }
        });

        // Unbind to ensure no error occurs (test will fail if it does)
        binding.unbind();
    }

/**
	 * Test for {@link ListBindings#selection(Object, String, JComboBox). Verifies binding updates correctly in both
	 * directions.
     *
	 * @throws InvocationTargetException 
	 * @throws InterruptedException 
	 */
    @Test
    public void testSelectionComboBox() throws InterruptedException, InvocationTargetException {
        // Setup
        final TestBean bean = TestBean.newInstance();
        final JComboBox comboBox = new JComboBox();
        final String value1 = "value1";
        final String value2 = "value2";

        // Bind
        Binding<TestBean, ListModel, Object, ListModel> binding = ListBindings.selection(bean, "string", comboBox);
        binding.bind();

        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, comboBox.getSelectedItem());
            }
        });
        // Combo box model has no items (no data binding) so setting the selected item does nothing
        comboBox.setSelectedItem(value1);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, comboBox.getSelectedItem());
            }
        });
        comboBox.setSelectedItem(null);

        // Manually populate the combo box (not via data binding), defaults selection to the first item.
        comboBox.setModel(new DefaultComboBoxModel(new String[] { value1, value2 }));
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value1, comboBox.getSelectedItem());
                assertEquals(value1, bean.getString());
            }
        });
        comboBox.setSelectedItem(value2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value2, bean.getString());
            }
        });
        comboBox.setSelectedItem(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, bean.getString());
            }
        });

        // Unbind to ensure no error occurs (test will fail if it does)
        binding.unbind();
    }

    /**
     * Test for {@link ListBindings#model(Object, String, javax.swing.JTable)}. Verifies binding in both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testModelTable() throws InterruptedException, InvocationTargetException {
        // Setup
        final JTable table = new JTable();
        // The bean list must be set prior to binding for it to be bound to the list instance
        final List<TestBean> l = new ArrayList<TestBean>();
        final TestBean bean = TestBean.newInstance();
        final Date date = Calendar.getInstance().getTime();
        for (int i = 0; i < 3; i++) {
            final TestBean b = TestBean.newInstance();
            b.setString("value" + i);
            b.setDate(date);
            b.setDuble(Double.valueOf(i));
            l.add(b);
        }
        final ObservableList<TestBean> list = ObservableCollections.observableList(l);
        bean.setTestBeans(list);

        // Bind
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("string", "String");
        map.put("duble", "Double");
        map.put("date", "Date");
        JTableBinding<Object, TestBean, JTable> binding = ListBindings.model(bean, "testBeans", table, map);
        binding.bind();

        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ListBindingsFunctionalTest.this.logger.info("size = " + list.size());
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    TestBean entry = list.get(i);
                    assertEquals(entry.getString(), table.getModel().getValueAt(i, 0));
                    assertEquals(entry.getString(), table.getValueAt(i, 0));
                    assertEquals(entry.getDuble(), table.getModel().getValueAt(i, 1));
                    assertEquals(entry.getDuble(), table.getValueAt(i, 1));
                    assertEquals(entry.getDate(), table.getModel().getValueAt(i, 2));
                    assertEquals(entry.getDate(), table.getValueAt(i, 2));
                }
            }
        });

        // Add another entry into the list
        int i = list.size();
        final TestBean b = TestBean.newInstance();
        b.setString("value" + i);
        b.setDate(date);
        b.setDuble(Double.valueOf(i));
        list.add(b);
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ListBindingsFunctionalTest.this.logger.info("size = " + list.size());
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    TestBean entry = list.get(i);
                    assertEquals(entry.getString(), table.getModel().getValueAt(i, 0));
                    assertEquals(entry.getString(), table.getValueAt(i, 0));
                    assertEquals(entry.getDuble(), table.getModel().getValueAt(i, 1));
                    assertEquals(entry.getDuble(), table.getValueAt(i, 1));
                    assertEquals(entry.getDate(), table.getModel().getValueAt(i, 2));
                    assertEquals(entry.getDate(), table.getValueAt(i, 2));
                }
            }
        });

        // Remove an entry from the list
        list.remove(0);
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ListBindingsFunctionalTest.this.logger.info("size = " + list.size());
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    TestBean entry = list.get(i);
                    assertEquals(entry.getString(), table.getModel().getValueAt(i, 0));
                    assertEquals(entry.getString(), table.getValueAt(i, 0));
                    assertEquals(entry.getDuble(), table.getModel().getValueAt(i, 1));
                    assertEquals(entry.getDuble(), table.getValueAt(i, 1));
                    assertEquals(entry.getDate(), table.getModel().getValueAt(i, 2));
                    assertEquals(entry.getDate(), table.getValueAt(i, 2));
                }
            }
        });

        // Change an entry in the list
        TestBean change = list.get(0);
        change.setDuble(Double.valueOf(10));
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ListBindingsFunctionalTest.this.logger.info("size = " + list.size());
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    TestBean entry = list.get(i);
                    assertEquals(entry.getString(), table.getModel().getValueAt(i, 0));
                    assertEquals(entry.getString(), table.getValueAt(i, 0));
                    ListBindingsFunctionalTest.this.logger.info("duble = " + entry.getDuble());
                    assertEquals(entry.getDuble(), table.getModel().getValueAt(i, 1));
                    assertEquals(entry.getDuble(), table.getValueAt(i, 1));
                    assertEquals(entry.getDate(), table.getModel().getValueAt(i, 2));
                    assertEquals(entry.getDate(), table.getValueAt(i, 2));
                }
            }
        });

        // Change an entry in the table
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                table.setValueAt(Double.valueOf(20), 0, 1);
            }
        });
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ListBindingsFunctionalTest.this.logger.info("size = " + list.size());
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    TestBean entry = list.get(i);
                    assertEquals(entry.getString(), table.getModel().getValueAt(i, 0));
                    assertEquals(entry.getString(), table.getValueAt(i, 0));
                    ListBindingsFunctionalTest.this.logger.info("duble = " + entry.getDuble());
                    assertEquals(entry.getDuble(), table.getModel().getValueAt(i, 1));
                    assertEquals(entry.getDuble(), table.getValueAt(i, 1));
                    assertEquals(entry.getDate(), table.getModel().getValueAt(i, 2));
                    assertEquals(entry.getDate(), table.getValueAt(i, 2));
                }
            }
        });

        // Changing the table model updates the bean! Do this test last.
        table.setModel(new DefaultTableModel());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertNull(bean.getStringList());
                assertEquals(0, table.getModel().getRowCount());
            }
        });

        // Unbind to ensure no error occurs (test will fail if it does)
        binding.unbind();
    }

    /**
     * Test for {@link ListBindings#selection(Object, String, JTable)}. Verifies binding in both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testSingleSelectionTable() throws InterruptedException, InvocationTargetException {
        // Setup
        final JTable table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // The bean list must be set prior to binding for it to be bound to the list instance
        final List<TestBean> l = new ArrayList<TestBean>();
        final TestBean bean = TestBean.newInstance();
        final Date date = Calendar.getInstance().getTime();
        for (int i = 0; i < 3; i++) {
            final TestBean b = TestBean.newInstance();
            b.setString("value" + i);
            b.setDate(date);
            b.setDuble(Double.valueOf(i));
            l.add(b);
        }
        final ObservableList<TestBean> list = ObservableCollections.observableList(l);
        bean.setTestBeans(list);

        final List<TestBean> s = new ArrayList<TestBean>();
        final ObservableList<TestBean> selected = ObservableCollections.observableList(s);
        bean.setTestBeansSelected(selected);

        // Bind
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("string", "String");
        map.put("duble", "Double");
        map.put("date", "Date");
        JTableBinding<Object, TestBean, JTable> listBinding = ListBindings.model(bean, "testBeans", table, map);
        listBinding.bind();
        Binding<Object, List<TestBean>, TestBean, List<TestBean>> selectionBinding = ListBindings.selection(bean,
                        "testBeansSelected", table);
        selectionBinding.bind();

        // Table selection binding is currently read-only (restriction of better beans binding). Adding an entry to the
        // selection list will have no effect on the table.
        selected.add(list.get(0));
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    assertEquals(0, table.getSelectedRowCount());
                    assertEquals(1, selected.size());
                    assertEquals(0, bean.getTestBeansSelected().size());
                }
            }
        });
        selected.clear();

        // Select a row in the table
        for (int i = 0; i < list.size(); i++) {
            final Integer select = i;
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    table.getSelectionModel().setSelectionInterval(select, select);
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // expect selected list contains the selected row
                    assertEquals(1, bean.getTestBeansSelected().size());
                    assertEquals(list.get(select), bean.getTestBeansSelected().get(0));
                }
            });
        }

        // Select contiguous rows in the table
        for (int i = 0; i < list.size(); i++) {
            final Integer selectFirst = 0;
            final Integer selectLast = i;
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    table.getSelectionModel().setSelectionInterval(selectFirst, selectLast);
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // being single selection only the last index should be selected
                    assertEquals(1, bean.getTestBeansSelected().size());
                    assertEquals(list.get(selectLast), bean.getTestBeansSelected().get(0));
                }
            });
        }

        listBinding.unbind();
        selectionBinding.unbind();
    }

    /**
     * Test for {@link ListBindings#selection(Object, String, JTable)}. Verifies binding in both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testSingleIntervalSelectionTable() throws InterruptedException, InvocationTargetException {
        // Setup
        final JTable table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        // The bean list must be set prior to binding for it to be bound to the list instance
        final List<TestBean> l = new ArrayList<TestBean>();
        final TestBean bean = TestBean.newInstance();
        final Date date = Calendar.getInstance().getTime();
        for (int i = 0; i < 3; i++) {
            final TestBean b = TestBean.newInstance();
            b.setString("value" + i);
            b.setDate(date);
            b.setDuble(Double.valueOf(i));
            l.add(b);
        }
        final ObservableList<TestBean> list = ObservableCollections.observableList(l);
        bean.setTestBeans(list);

        final List<TestBean> s = new ArrayList<TestBean>();
        final ObservableList<TestBean> selected = ObservableCollections.observableList(s);
        bean.setTestBeansSelected(selected);

        // Bind
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("string", "String");
        map.put("duble", "Double");
        map.put("date", "Date");
        JTableBinding<Object, TestBean, JTable> listBinding = ListBindings.model(bean, "testBeans", table, map);
        listBinding.bind();
        Binding<Object, List<TestBean>, TestBean, List<TestBean>> selectionBinding = ListBindings.selection(bean,
                        "testBeansSelected", table);
        selectionBinding.bind();

        // Table selection binding is currently read-only (restriction of better beans binding). Adding an entry to the
        // selection list will have no effect on the table.
        selected.add(list.get(0));
        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(list.size(), table.getModel().getRowCount());
                for (int i = 0; i < list.size(); i++) {
                    assertEquals(0, table.getSelectedRowCount());
                    assertEquals(1, selected.size());
                    assertEquals(0, bean.getTestBeansSelected().size());
                }
            }
        });
        selected.clear();

        // Select a row in the table
        for (int i = 0; i < list.size(); i++) {
            final Integer select = i;
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    table.getSelectionModel().setSelectionInterval(select, select);
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // expect selected list contains the selected row
                    assertEquals(1, bean.getTestBeansSelected().size());
                    assertEquals(list.get(select), bean.getTestBeansSelected().get(0));
                }
            });
        }

        // Select contiguous rows in the table
        for (int i = 0; i < list.size(); i++) {
            final Integer selectFirst = 0;
            final Integer selectLast = i;
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    table.getSelectionModel().setSelectionInterval(selectFirst, selectLast);
                }
            });
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    // expect the selected list contains the selected rows
                    assertEquals(selectLast.intValue() + 1, bean.getTestBeansSelected().size());
                    assertEquals(list.get(selectLast), bean.getTestBeansSelected().get(selectLast));
                }
            });
        }

        listBinding.unbind();
        selectionBinding.unbind();
    }
}
