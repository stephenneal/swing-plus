package com.swingplus.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beansbinding.Binding;
import org.jdesktop.observablecollections.ObservableList;

import com.swingplus.ApplicationContext;
import com.swingplus.PropertyChangeSupport2;
import com.swingplus.bind.bbb.AbstractModel;
import com.swingplus.bind.bbb.BindingService;
import com.swingplus.bind.bbb.TextBindings;

@SuppressWarnings("serial")
public class BetterBeansBindingPrototype extends JPanel {

    public static void main(String[] args) {
        ApplicationContext.getInstance().setBindingService(new BindingService());
        final BetterBeansBindingPrototype prototype = createPrototype();
        final JFrame frame = new JFrame("Swing Plus Binding Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                ApplicationContext.getInstance().release();
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
        model.setField1Label("String");
        model.setField2Label("Double");
        model.setField3Label("Date");
        model.setField4Label("Date/Time");
        model.setString("Some value");
        model.setDuble(new Double(20.00));
        model.setDate(Calendar.getInstance().getTime());
        model.setDateTime(Calendar.getInstance().getTime());
        return new BetterBeansBindingPrototype(model);
    }

    // Input components
    private JLabel field1Label;
    private JTextField field1;
    private JLabel field2Label;
    private JTextField field2;
    private JLabel field3Label;
    private JTextField field3;
    private JLabel field4Label;
    private JTextField field4;

    // Output components
    private JLabel field1OutputLabel;
    private JLabel field1Output;
    private JLabel field2OutputLabel;
    private JLabel field2Output;
    private JLabel field3OutputLabel;
    private JLabel field3Output;
    private JLabel field4OutputLabel;
    private JLabel field4Output;

    public BetterBeansBindingPrototype(BindingPrototypeModel model) {
        super();
        // Create all the Swing components
        this.field1Label = new JLabel();
        this.field1 = new JTextField();
        this.field2Label = new JLabel();
        this.field2 = new JTextField();
        this.field3Label = new JLabel();
        this.field3 = new JTextField();
        this.field4Label = new JLabel();
        this.field4 = new JTextField();

        this.field1OutputLabel = new JLabel();
        this.field1Output = new JLabel();
        this.field1Output.setBorder(BorderFactory.createEtchedBorder());
        this.field2OutputLabel = new JLabel();
        this.field2Output = new JLabel();
        this.field2Output.setBorder(BorderFactory.createEtchedBorder());
        this.field3OutputLabel = new JLabel();
        this.field3Output = new JLabel();
        this.field3Output.setBorder(BorderFactory.createEtchedBorder());
        this.field4OutputLabel = new JLabel();
        this.field4Output = new JLabel();
        this.field4Output.setBorder(BorderFactory.createEtchedBorder());

        // Bind the Swing components to the model
        bind(TextBindings.text(model, "field1Label", this.field1Label));
        bind(TextBindings.text(model, "field2Label", this.field2Label));
        bind(TextBindings.text(model, "field3Label", this.field3Label));
        bind(TextBindings.text(model, "field4Label", this.field4Label));
        bind(TextBindings.text(model, "field1Label", this.field1OutputLabel));
        bind(TextBindings.text(model, "field2Label", this.field2OutputLabel));
        bind(TextBindings.text(model, "field3Label", this.field3OutputLabel));
        bind(TextBindings.text(model, "field4Label", this.field4OutputLabel));

        bind(TextBindings.text(model, "string", this.field1));
        bind(TextBindings.text(model, "duble", this.field2));
        bind(TextBindings.text(model, "date", this.field3));
        bind(TextBindings.text(model, "dateTime", this.field4, DateFormat.getDateTimeInstance()));

        bind(TextBindings.text(model, "string", this.field1Output));
        bind(TextBindings.text(model, "duble", this.field2Output));
        bind(TextBindings.text(model, "date", this.field3Output));
        bind(TextBindings.text(model, "dateTime", this.field4Output, DateFormat.getDateTimeInstance()));

        // Lay the components. For simplicity this just uses box layout, its not pretty. Better Swing layouts
        // include MigLayout and JGoodies FormLayout but that is outside the scope of this prototype.
        JPanel input = box(BoxLayout.Y_AXIS);
        input.setBorder(BorderFactory.createTitledBorder("Input"));
        input.add(box(BoxLayout.X_AXIS, this.field1Label, this.field1));
        input.add(box(BoxLayout.X_AXIS, this.field2Label, this.field2));
        input.add(box(BoxLayout.X_AXIS, this.field3Label, this.field3));
        input.add(box(BoxLayout.X_AXIS, this.field4Label, this.field4));

        JPanel output = box(BoxLayout.Y_AXIS);
        output.setBorder(BorderFactory.createTitledBorder("Output"));
        output.add(box(BoxLayout.X_AXIS, this.field1OutputLabel, this.field1Output));
        output.add(box(BoxLayout.X_AXIS, this.field2OutputLabel, this.field2Output));
        output.add(box(BoxLayout.X_AXIS, this.field3OutputLabel, this.field3Output));
        output.add(box(BoxLayout.X_AXIS, this.field4OutputLabel, this.field4Output));

        box(this, BoxLayout.Y_AXIS, input, output);
    }

    private void bind(Binding<?, ?, ?, ?> binding) {
        if (binding == null) {
            return;
        }
        ApplicationContext.getInstance().getBindingService().bind(binding);
    }

    /**
     * Creates a new panel with {@link BoxLayout} and adds the components.
     * 
     * @param axis see {@link BoxLayout#BoxLayout(java.awt.Container, int)} axis
     * @param components (optional) components to add
     * @return a new panel with {@link BoxLayout} and the components.
     */
    private static JPanel box(int axis, JComponent... components) {
        return box(new JPanel(), axis, components);
    }

    /**
     * Sets the layout of the panel to {@link BoxLayout} and adds the components.
     * 
     * @param axis see {@link BoxLayout#BoxLayout(java.awt.Container, int)} axis
     * @param components (optional) components to add
     * @return the panel with {@link BoxLayout} and the components.
     */
    private static JPanel box(JPanel panel, int axis, JComponent... components) {
        panel.setLayout(new BoxLayout(panel, axis));
        if (components != null) {
            for (JComponent c : components) {
                panel.add(c);
            }
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
        private String field4Label;

        private Date date;
        private Date dateTime;
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

        public String getField4Label() {
            return this.field4Label;
        }

        public void setField4Label(String newValue) {
            String oldValue = this.field4Label;
            this.field4Label = newValue;
            getPropertyChangeSupport().firePropertyChange("field4Label", oldValue, newValue);
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date newValue) {
            Date oldValue = this.date;
            this.date = newValue;
            getPropertyChangeSupport().firePropertyChange("date", oldValue, newValue);
        }

        public Date getDateTime() {
            return this.dateTime;
        }

        public void setDateTime(Date newValue) {
            Date oldValue = this.dateTime;
            this.dateTime = newValue;
            getPropertyChangeSupport().firePropertyChange("dateTime", oldValue, newValue);
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
