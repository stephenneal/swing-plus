package com.swing.plus.demo;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GroupLayoutPrototype {

    private enum Demo {
        DEFAULT, GROUP_LAYOUT, GROUP_LAYOUT_WITH_BUTTONS
    }

    public static void main(String[] args) {
        final JFrame frame = createFrame(Demo.GROUP_LAYOUT_WITH_BUTTONS);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static JFrame createFrame(Demo d) {
        // 1. Create the frame.
        JFrame frame = new JFrame("Swing Plus Demo");

        // 2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Create components and put them in the frame.
        frame.getContentPane().add(createPanel(d), BorderLayout.CENTER);

        // 4. Size the frame.
        frame.pack();

        return frame;
    }

    private static JPanel createPanel(Demo d) {
        switch (d) {
        case GROUP_LAYOUT:
            return createGroupLayoutBasicPanel();
        case GROUP_LAYOUT_WITH_BUTTONS:
            return createGroupLayoutWithButtonsPanel();
        default:
            return createDefaultPanel();
        }
    }

    private static JPanel createDefaultPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Field 1"));
        panel.add(new JTextField(15));
        panel.add(new JLabel("Field 2"));
        panel.add(new JTextField(15));
        return panel;
    }

    private static JPanel createGroupLayoutBasicPanel() {
        JLabel label1 = new JLabel("Field 1");
        JTextField tf1 = new JTextField(15);
        JLabel label2 = new JLabel("Field 2");
        JTextField tf2 = new JTextField(15);

        JPanel panel = new JPanel();
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
        hGroup.addGroup(layout.createParallelGroup().addComponent(label1).addComponent(label2));
        hGroup.addGroup(layout.createParallelGroup().addComponent(tf1).addComponent(tf2));
        layout.setHorizontalGroup(hGroup);

        // Create a sequential group for the vertical axis.
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        // The sequential group contains two parallel groups that align
        // the contents along the baseline. The first parallel group contains
        // the first label and text field, and the second parallel group contains
        // the second label and text field. By using a sequential group
        // the labels and text fields are positioned vertically after one another.
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label1).addComponent(tf1));
        vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(label2).addComponent(tf2));
        layout.setVerticalGroup(vGroup);
        return panel;
    }

    private static JPanel createGroupLayoutWithButtonsPanel() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        JButton buttonD = new JButton("D");
        JButton buttonR = new JButton("R");
        JButton buttonY = new JButton("Y");
        JButton buttonO = new JButton("O");
        JButton buttonT = new JButton("T");

        GroupLayout.SequentialGroup leftToRight = layout.createSequentialGroup();

        leftToRight.addComponent(buttonD);
        GroupLayout.ParallelGroup columnMiddle = layout.createParallelGroup();
        columnMiddle.addComponent(buttonR);
        columnMiddle.addComponent(buttonO);
        columnMiddle.addComponent(buttonT);
        leftToRight.addGroup(columnMiddle);
        leftToRight.addComponent(buttonY);

        GroupLayout.SequentialGroup topToBottom = layout.createSequentialGroup();
        GroupLayout.ParallelGroup rowTop = layout.createParallelGroup();
        rowTop.addComponent(buttonD);
        rowTop.addComponent(buttonR);
        rowTop.addComponent(buttonY);
        topToBottom.addGroup(rowTop);
        topToBottom.addComponent(buttonO);
        topToBottom.addComponent(buttonT);

        layout.setHorizontalGroup(leftToRight);
        layout.setVerticalGroup(topToBottom);
        return panel;
    }

}
