package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MyFrame extends JFrame {


    public String content;

    public File file;
    public StringBuilder sb = new StringBuilder();
    public JFileChooser fileChooser = new JFileChooser();
    public JFileChooser fileSave = new JFileChooser();

    public JMenuBar menuBar = new JMenuBar();
    public JMenu fileMenu = new JMenu("文件");
    public JMenu editMenu = new JMenu("设置");
    public JMenu formatMenu = new JMenu("历史记录");


    public JMenuItem selectItem = new JMenuItem("选择文件");
    public JMenuItem saveItem = new JMenuItem("保存");
    public JMenuItem configItem = new JMenuItem("配置");

    public JCheckBox TimeStamp = new JCheckBox("时间戳");

    public JTextArea area = new JTextArea();;
    public JScrollPane textPanel = new JScrollPane(area);

    public JButton chooseBtn = new JButton("选择");;
    public JButton submitBtn = new JButton("提交");
    public JButton getResultBtn = new JButton("获取");
    public JButton clearPanelBtn = new JButton("清空");
    public JPanel btnPanel = new JPanel();
    public Controller controller = new Controller(this);

    MyFrame() {

        fileChooser.setMultiSelectionEnabled(false);             //设为多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("mp3,w4p,wav", "mp3","w4p","wav"));
        fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSave.setCurrentDirectory(fileSave.getSelectedFile());
        fileSave.setFileFilter(new FileNameExtensionFilter("doc,docx", "docx","doc"));
        fileSave.setFileFilter(new FileNameExtensionFilter("txt", "txt"));
        fileSave.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileSave.setDialogTitle("保存数据");

        this.setLayout(new BorderLayout());

        selectItem.addActionListener(controller.fileRelatedListener);
        fileMenu.add(selectItem);
        fileMenu.add(new JPopupMenu.Separator());
        saveItem.addActionListener(controller.fileRelatedListener);
        fileMenu.add(saveItem);

        configItem.addActionListener(controller.showConfigPanelListener);
        editMenu.add(configItem);
        editMenu.add(new JPopupMenu.Separator());
        editMenu.add(TimeStamp);
        TimeStamp.addItemListener(controller.timeStampChangeListener);
        TimeStamp.setSelected(Property.propMap.get("TimeStamp").equals("0"));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("", 0, 18));
        textPanel.setPreferredSize(new Dimension(800, 600));

        chooseBtn.addActionListener(controller.fileRelatedListener);
        submitBtn.addActionListener(controller.submitListener);
        getResultBtn.addActionListener(controller.getResListener);
        clearPanelBtn.addActionListener(controller.clearPanelListener);
        btnPanel.setPreferredSize(new Dimension(820, 50));
        btnPanel.add(chooseBtn);
        btnPanel.add(submitBtn);
        btnPanel.add(getResultBtn);
        btnPanel.add(clearPanelBtn);

        this.setTitle("语音识别");
        this.setJMenuBar(menuBar);
        this.add(textPanel, BorderLayout.CENTER);
        this.add(btnPanel, BorderLayout.SOUTH);
        this.setSize(813, 700);
        this.setLocation(300, 50);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}

