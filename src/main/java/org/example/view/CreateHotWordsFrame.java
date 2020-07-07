package org.example.view;

import javax.swing.*;
import java.awt.*;

public class CreateHotWordsFrame extends JPanel {

    JPanel panel0 = new JPanel();
    public JPanel panel1 = new JPanel();
    JLabel idLabel = new JLabel("              id:");
    public JTextField id = new JTextField();
    JPanel panel2 = new JPanel();
    JLabel nameLabel = new JLabel("热词表名:");
    public JTextField name = new JTextField();

    JPanel panel3 = new JPanel();
    JLabel describeLabel = new JLabel("         描述:");
    public JTextField describe = new JTextField();

    public JTextArea input = new JTextArea();
    JScrollPane panel4 = new JScrollPane(input);

    public JCheckBox isDefault = new JCheckBox("设为默认");

    public StringBuilder content = new StringBuilder();

    public CreateHotWordsFrame(){
        this.setLayout(new BorderLayout());
        panel0.setLayout(new BorderLayout());

        id.setPreferredSize(new Dimension(150,20));
        name.setPreferredSize(new Dimension(150,20));
        describe.setPreferredSize(new Dimension(150,20));

        panel1.add(idLabel,BorderLayout.WEST);
        panel1.add(id,BorderLayout.CENTER);
        panel2.add(nameLabel, BorderLayout.WEST);
        panel2.add(name, BorderLayout.CENTER);

        input.setWrapStyleWord(true);
        input.setLineWrap(true);
        input.setText("示例：敏捷编程|5（热词|权重）");
        panel4.setPreferredSize(new Dimension(200,200));
        panel3.add(describeLabel);
        panel3.add(describe);
        panel0.add(panel1, BorderLayout.NORTH);
        panel0.add(panel2,BorderLayout.CENTER);
        panel0.add(panel3,BorderLayout.SOUTH);
        this.add(panel0, BorderLayout.NORTH);
        this.add(panel4, BorderLayout.CENTER);
        this.add(isDefault, BorderLayout.SOUTH);
    }

}
