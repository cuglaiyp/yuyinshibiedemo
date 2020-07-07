package org.example.view;

import org.example.controller.Controller;
import org.example.util.Property;

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
    public JMenu hotWordsMenu = new JMenu("热词");


    public JMenuItem selectItem = new JMenuItem("选择文件");
    public JMenuItem saveItem = new JMenuItem("保存");
    // -----------------------------------------------------
    public JMenuItem configItem = new JMenuItem("配置");
    public JCheckBoxMenuItem TimeStamp = new JCheckBoxMenuItem("时间戳");
    // -----------------------------------------------------------------
    public JMenuItem createHotWords = new JMenuItem("创建热词表");
    public JMenuItem deleteHotWords = new JMenuItem("删除热词表");
    public JMenuItem listHotWords = new JMenuItem("列举热词表");
    public JMenuItem updateHotWords = new JMenuItem("更新热词表");
    public JMenu useHotWords = new JMenu("使用热词表");

    public JTextArea area = new JTextArea();;
    public JScrollPane textPanel = new JScrollPane(area);

    public JButton chooseBtn = new JButton("选择");;
    public JButton submitBtn = new JButton("提交");
    public JButton getResultBtn = new JButton("获取");
    public JButton clearPanelBtn = new JButton("清空");
    public JPanel btnPanel = new JPanel();
    public Controller controller = new Controller(this);

    public MyFrame() {

        fileChooser.setMultiSelectionEnabled(false);             //设为多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("mp3,w4p,wav", "mp3", "w4p", "wav"));
        fileChooser.setCurrentDirectory(fileChooser.getSelectedFile());
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSave.setCurrentDirectory(fileSave.getSelectedFile());
        fileSave.setFileFilter(new FileNameExtensionFilter("doc,docx", "docx", "doc"));
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

        createHotWords.addActionListener(controller.hotWordsPanelListener);
        listHotWords.addActionListener(controller.listVocabsListener);
        updateHotWords.addActionListener(controller.updateVocabsListener);
        deleteHotWords.addActionListener(controller.deleteVocabsListener);

        hotWordsMenu.addMenuListener(controller.initialVocabs);

        hotWordsMenu.add(createHotWords);
        hotWordsMenu.add(new JPopupMenu.Separator());
        hotWordsMenu.add(deleteHotWords);
        hotWordsMenu.add(new JPopupMenu.Separator());
        hotWordsMenu.add(listHotWords);
        hotWordsMenu.add(new JPopupMenu.Separator());
        hotWordsMenu.add(updateHotWords);
        hotWordsMenu.add(new JPopupMenu.Separator());
        hotWordsMenu.add(useHotWords);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(hotWordsMenu);

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

