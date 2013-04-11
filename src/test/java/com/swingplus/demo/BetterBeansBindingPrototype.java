package com.swingplus.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.observablecollections.ObservableList;

import com.swingplus.PropertyChangeSupport2;
import com.swingplus.bind.bbb.AbstractModel;
import com.swingplus.bind.bbb.Binder;

@SuppressWarnings("serial")
public class BetterBeansBindingPrototype extends JPanel {

    public static void main(String[] args) {
        final BetterBeansBindingPrototype prototype = createPrototype();
        final JFrame frame = new JFrame("Swing Plus Binding Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                prototype.release();
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.getContentPane().add(prototype, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    private static BetterBeansBindingPrototype createPrototype() {
        BindingPrototypeModel model = BindingPrototypeModel.newInstance();
        model.setField1Label("Field 1");
        model.setField2Label("Field 2");
        model.setField3Label("Field 3");
        model.setString("Some value");
        model.setDuble(new Double(20.00));
        model.setDate(Calendar.getInstance().getTime());
        return new BetterBeansBindingPrototype(model);
    }

    private JLabel text1Label;
    private JTextField text1;
    private JLabel text2Label;
    private JTextField text2;
    private JLabel text3Label;
    private JTextField text3;
    private Binder<BindingPrototypeModel> binder;

    private JLabel text1Output;
    private JLabel text2Output;
    private JLabel text3Output;

    public BetterBeansBindingPrototype(BindingPrototypeModel model) {
        this.text1Label = new JLabel();
        this.text1 = new JTextField();
        this.text2Label = new JLabel();
        this.text2 = new JTextField();
        this.text3Label = new JLabel();
        this.text3 = new JTextField();

        this.text1Output = new JLabel();
        this.text1Output.setMinimumSize(new Dimension(25, this.text1Output.getHeight()));
        this.text2Output = new JLabel();
        this.text1Output.setMinimumSize(new Dimension(25, this.text2Output.getHeight()));
        this.text3Output = new JLabel();
        this.text1Output.setMinimumSize(new Dimension(25, this.text3Output.getHeight()));

        this.binder = new Binder<BindingPrototypeModel>(model);
        this.binder.bindText(this.text1Label, "field1Label");
        this.binder.bindText(this.text2Label, "field2Label");
        this.binder.bindText(this.text3Label, "field3Label");
        this.binder.bindText(this.text1, "string");
        this.binder.bindText(this.text2, "duble");
        this.binder.bindText(this.text3, "date");
        this.binder.bindText(this.text1Output, "string");
        this.binder.bindText(this.text2Output, "duble");
        this.binder.bindText(this.text3Output, "date");

        layoutComponentsBox(this);
    }

    public void release() {
        this.binder.release();
    }

    // Lay the components out in a box. NB. Preferred Swing layouts include MigLayout and JGoodies FormLayout.
    private void layoutComponentsBox(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createHBox(this.text1Label, this.text1, this.text1Output));
        panel.add(createHBox(this.text2Label, this.text2, this.text2Output));
        panel.add(createHBox(this.text3Label, this.text3, this.text3Output));
    }

    private static JPanel createHBox(JComponent... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        for (JComponent c : components) {
            panel.add(c);
        }
        return panel;
    }

    /**
     * Model for this prototype, must be public for BetterBeansBinding to work
     */
    public static class BindingPrototypeModel extends AbstractModel {

        public static BindingPrototypeModel newInstance() {
            BindingPrototypeModel bean = new BindingPrototypeModel();
            bean.setPropertyChangeSupport(new PropertyChangeSupport2(bean));
            return bean;
        }

        private String field1Label;
        private String field2Label;
        private String field3Label;

        private Date date;
        private Double duble;
        private ObservableList<String> stringList;
        private String string;
        private ObservableList<BindingPrototypeModel> testBeans;
        private ObservableList<BindingPrototypeModel> testBeansSelected;

        private BindingPrototypeModel() {
            super();
        }

        public String getField1Label() {
            return this.field1Label;
        }

        public void setField1Label(String newValue) {
            String oldValue = this.field1Label;
            this.field1Label = newValue;
            getPropertyChangeSupport().firePropertyChange("field1Label", oldValue, newValue);
        }

        public String getField2Label() {
            return this.field2Label;
        }

        public void setField2Label(String newValue) {
            String oldValue = this.field2Label;
            this.field2Label = newValue;
            getPropertyChangeSupport().firePropertyChange("field2Label", oldValue, newValue);
        }

        public String getField3Label() {
            return this.field3Label;
        }

        public void setField3Label(String newValue) {
            String oldValue = this.field3Label;
            this.field3Label = newValue;
            getPropertyChangeSupport().firePropertyChange("field3Label", oldValue, newValue);
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date newValue) {
            Date oldValue = this.date;
            this.date = newValue;
            getPropertyChangeSupport().firePropertyChange("date", oldValue, newValue);
        }

        public Double getDuble() {
            return this.duble;
        }

        public void setDuble(Double newValue) {
            Double oldValue = this.duble;
            this.duble = newValue;
            getPropertyChangeSupport().firePropertyChange("duble", oldValue, newValue);
        }

        public ObservableList<String> getStringList() {
            return this.stringList;
        }

        public void setStringList(ObservableList<String> newValue) {
            ObservableList<String> oldValue = this.stringList;
            this.stringList = newValue;
            getPropertyChangeSupport().firePropertyChange("stringList", oldValue, newValue);
        }

        public String getString() {
            return this.string;
        }

        public void setString(String newValue) {
            String oldValue = this.string;
            this.string = newValue;
            getPropertyChangeSupport().firePropertyChange("string", oldValue, newValue);
        }

        public ObservableList<BindingPrototypeModel> getTestBeans() {
            return this.testBeans;
        }

        public void setTestBeans(ObservableList<BindingPrototypeModel> newValue) {
            ObservableList<BindingPrototypeModel> oldValue = this.testBeans;
            this.testBeans = newValue;
            getPropertyChangeSupport().firePropertyChange("testBeans", oldValue, newValue);
        }

        public ObservableList<BindingPrototypeModel> getTestBeansSelected() {
            return this.testBeansSelected;
        }

        public void setTestBeansSelected(ObservableList<BindingPrototypeModel> newValue) {
            ObservableList<BindingPrototypeModel> oldValue = this.testBeansSelected;
            this.testBeansSelected = newValue;
            getPropertyChangeSupport().firePropertyChange("testBeansSelected", oldValue, newValue);
        }

    }

}
