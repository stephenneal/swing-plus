package com.swingplus.bind.bbb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
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
     * Test {@link Binder#bindText(JTextComponent, String)}. NB. If this test fails check the test for the underlying
     * binding help.
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
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)}.
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
        // Set the text field to null
        label.setText(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Update the text field with a value
        label.setText(value.toString());
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
                assertEquals(null, label.getText());
            }
        });
        // Update the text field with a value
        label.setText(value.toString());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
        // Set the text field to an empty string. This will fail conversion and the previous value is retained (grrr)
        label.setText("");
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getIntegr());
            }
        });
    }

    /**
     * Test {@link Binder#bindText(JComponent, String, Class<T> fieldType)}.
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
     * Test {@link Binder#bindText(JTextComponent, String, Class<T> fieldType)}. NB. If this test fails check the test
     * for the underlying binding help.
     * <p>
     * TODO copy for other types, i.e. Integer, Date etc
     * </p>
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

    // /**
    // * Test {@link Binder#bindDate(JDateChooser, String)}. NB. If this test fails check the test for the underlying
    // * binding help.
    // *
    // * @throws InvocationTargetException
    // * @throws InterruptedException
    // */
    // @Test
    // public void testBindDate() throws InterruptedException, InvocationTargetException {
    // // Setup
    // final JDateChooser dateField = new JDateChooser();
    //
    // // Bind
    // this.binder.bindDate(dateField, "date");
    //
    // // Test
    // assertEquals(null, this.binder.getBean().getString());
    // assertEquals(null, dateField.getDate());
    //
    // // Update the bean value
    // final Calendar cal = Calendar.getInstance();
    // final Date value = cal.getTime();
    // this.binder.getBean().setDate(value);
    // // Not sure why but this test fails occasionally, the helper test has not yet failed...
    // // Is there an EDT issue with JDateChooser?
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(value, dateField.getDate());
    // }
    // });
    // // Clear the date chooser
    // dateField.setDate(null);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(null, BinderIntegrationTest.this.binder.getBean().getDate());
    // }
    // });
    // // Update the date chooser with a value
    // dateField.setDate(value);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(value, BinderIntegrationTest.this.binder.getBean().getDate());
    // }
    // });
    // // Clear the bean value
    // this.binder.getBean().setDate(null);
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // assertEquals(null, dateField.getDate());
    // }
    // });
    // }

    /**
     * Test {@link Binder#bindJComboBox(javax.swing.JComboBox, String, String)}. NB. If this test fails check the test
     * for the underlying binding help.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testBindJComboBox() throws InterruptedException, InvocationTargetException {
        // Setup
        final JComboBox comboBox = new JComboBox();
        // The bean list must be set prior to binding for it to be bound to the list instance
        List<String> l = new ArrayList<String>();
        final String value1 = "value1";
        l.add(value1);
        final ObservableList<String> list = ObservableCollections.observableList(l);
        this.binder.getBean().setStringList(list);

        // Bind
        this.binder.bindJComboBox(comboBox, "string");

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
        this.binder.getBean().getStringList().add(value2);
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
        this.binder.getBean().getStringList().remove(value2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, comboBox.getModel().getSize());
                assertEquals(value1, comboBox.getModel().getElementAt(0));
                assertEquals(value1, comboBox.getItemAt(0));
            }
        });
        // Select an item in the list
        comboBox.setSelectedItem(value1);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value1, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Select an item that is not in the list, previous selection remains
        comboBox.setSelectedItem(value2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(value1, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });
        // Clear the selection
        comboBox.setSelectedItem(null);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(null, BinderIntegrationTest.this.binder.getBean().getString());
            }
        });

        // Changing the combo box model will not update the bean!
        comboBox.setModel(new DefaultComboBoxModel(new String[] { value1 }));
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertEquals(1, BinderIntegrationTest.this.binder.getBean().getStringList().size());
                assertEquals(value1, BinderIntegrationTest.this.binder.getBean().getStringList().get(0));
            }
        });
    }

    /**
     * Test for {@link Binder#bindJComboBox(JTable, String, java.util.Map)}. Verifies binding in both directions.
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void testBindJTable() throws InterruptedException, InvocationTargetException {
        // Setup
        final JTable table = new JTable();
        List<TestBean> l = new ArrayList<TestBean>();
        final Date date = Calendar.getInstance().getTime();
        for (int i = 0; i < 3; i++) {
            final TestBean b = TestBean.newInstance();
            b.setString("value" + i);
            b.setDate(date);
            b.setDuble(Double.valueOf(i));
            l.add(b);
        }
        final ObservableList<TestBean> list = ObservableCollections.observableList(l);
        // The bean list must be set prior to binding for it to be bound to the list instance (actually this might not
        // be true for tables)
        this.binder.getBean().setTestBeans(list);
        List<TestBean> s = new ArrayList<TestBean>();
        final ObservableList<TestBean> selected = ObservableCollections.observableList(s);
        this.binder.getBean().setTestBeansSelected(selected);

        // Bind
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("string", "String");
        map.put("duble", "Double");
        map.put("date", "Date");
        this.binder.bindJTable(table, "testBeans", map);

        // Test
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
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
                }
            }
        });

        // Select a row in the table, expect the selection to match the interval but testing has shown that no matter
        // what value is passed to select only the first row is ever selected. Is something wrong or am I expecting the
        // wrong behaviour from setSelectionInterval(...)
        for (i = 0; i < list.size(); i++) {
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
                    assertEquals(1, selected.size());
                    // Expect first row here for now, refer to comment above for explanation
                    assertEquals(list.get(0), selected.get(0));
                }
            });
        }

        // Select contiguous rows in the table, for single selection the last index should be selected but testing has
        // shown that no matter what value is passed to select only the first row is ever selected. Is something wrong
        // or am I expecting the wrong behaviour from setSelectionInterval(...)
        for (i = 1; i < list.size(); i++) {
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
                    assertEquals(1, selected.size());
                    // Expect first row here for now, refer to comment above for explanation
                    assertEquals(list.get(0), selected.get(0));
                }
            });
        }

        // Changing the table model updates the bean! Do this test last.
        table.setModel(new DefaultTableModel());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                assertNull(BinderIntegrationTest.this.binder.getBean().getStringList());
                assertEquals(0, table.getModel().getRowCount());
            }
        });
    }

}
