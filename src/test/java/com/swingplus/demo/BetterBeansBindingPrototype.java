package com.swingplus.demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
        model.setString("Some value");
        model.setDuble(new Double(20.00));
        model.setDate(Calendar.getInstance().getTime());
        return new BetterBeansBindingPrototype(model);
    }

    private JLabel text1Label;
    private JTextField text1;
    private JLabel text1Output;
    private JLabel text2Label;
    private JTextField text2;
    private JLabel text3Label;
    private JTextField text3;
    private Binder<BindingPrototypeModel> binder;

    public BetterBeansBindingPrototype(BindingPrototypeModel model) {
        this.text1Label = new JLabel("Text 1");
        this.text1 = new JTextField(15);
        this.text2Label = new JLabel("Text 2");
        this.text2 = new JTextField(15);
        this.text3Label = new JLabel("Text 3");
        this.text3 = new JTextField(15);

        this.text1Output = new JLabel();

        this.binder = new Binder<BindingPrototypeModel>(model);
        this.binder.bindText(this.text1, "string");
        this.binder.bindText(this.text1Output, "string");
        this.binder.bindText(this.text2, "duble");
        this.binder.bindText(this.text3, "date");

        layoutComponentsGroup(this);
    }

    public void release() {
        this.binder.release();
    }

    private void layoutComponentsBox(JPanel panel) {
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(this.text1Label);
        panel1.add(this.text1);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.add(this.text2Label);
        panel2.add(this.text2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
        panel3.add(this.text3Label);
        panel3.add(this.text3);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
    }

    private void layoutComponentsGroup(JPanel panel) {
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        // Turn on automatically adding gaps between components
        layout.setAutoCreateGaps(true);

        // Turn on automatically creating gaps between components that touch
        // the edge of the container and the container.
        layout.setAutoCreateContainerGaps(true);

        // Create a sequential group for the horizontal axis.
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

        // The sequential group in turn contains two parallel groups.
        // One parallel group contains the labels, the other the text fields.
        // Putting the labels in a parallel group along the horizontal axis
        // positions them at the same x location.
        //
        // Variable indentation is used to reinforce the level of grouping.
        hGroup.addGroup(layout.createParallelGroup().addComponent(this.text1Label).addComponent(this.text2Label)
                        .addComponent(this.text3Label));
        hGroup.addGroup(layout.createParallelGroup().addComponent(this.text1).addComponent(this.text2)
                        .addComponent(this.text3));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        // The sequential group contains two parallel groups that align
        // the contents along the baseline. The first parallel group contains
        // the first label and text field, and the second parallel group contains
        // the second label and text field. By using a sequential group
        // the labels and text fields are positioned vertically after one another.
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.text1Label)
                        .addComponent(this.text1));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.text2Label)
                        .addComponent(this.text2));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.text3Label)
                        .addComponent(this.text3));
        layout.setVerticalGroup(vGroup);
    }

    public static class BindingPrototypeModel extends AbstractModel {

        public static BindingPrototypeModel newInstance() {
            BindingPrototypeModel bean = new BindingPrototypeModel();
            bean.setPropertyChangeSupport(new PropertyChangeSupport2(bean));
            return bean;
        }

        private Date date;
        private Double duble;
        private ObservableList<String> stringList;
        private String string;
        private ObservableList<BindingPrototypeModel> testBeans;
        private ObservableList<BindingPrototypeModel> testBeansSelected;

        private BindingPrototypeModel() {
            super();
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
