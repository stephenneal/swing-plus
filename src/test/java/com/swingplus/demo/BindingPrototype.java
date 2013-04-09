package com.swingplus.demo;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.swingplus.bind.Binder;

@SuppressWarnings("serial")
public class BindingPrototype extends JPanel {

    public static void main(String[] args) {
        final JFrame frame = createFrame();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Swing Plus Binding Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        BindingPrototypeModel model = BindingPrototypeModel.newInstance();
        model.setString("Some value");
        model.setDuble(new Double(20.00));
        frame.getContentPane().add(new BindingPrototype(model), BorderLayout.CENTER);
        frame.pack();
        return frame;
    }

    private JLabel text1Label;
    private JTextField text1;
    private JLabel text2Label;
    private JTextField text2;
    private Binder<BindingPrototypeModel> binder;

    public BindingPrototype(BindingPrototypeModel model) {
        this.text1Label = new JLabel("Text 1");
        this.text1 = new JTextField(15);
        this.text2Label = new JLabel("Text 2");
        this.text2 = new JTextField(15);

        this.binder = new Binder<BindingPrototypeModel>(model);
        this.binder.bindText(this.text1, "duble", Double.class);
        // this.binder.bindText(this.text2, "duble", Double.class);

        layoutComponentsBox(this);
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

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panel1);
        panel.add(panel2);
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
        hGroup.addGroup(layout.createParallelGroup().addComponent(this.text1Label).addComponent(this.text2Label));
        hGroup.addGroup(layout.createParallelGroup().addComponent(this.text1).addComponent(this.text2));
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
        layout.setVerticalGroup(vGroup);
    }

}
