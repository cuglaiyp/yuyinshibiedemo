package org.example.controller;

import com.tencentcloudapi.asr.v20190614.models.*;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.Main;
import org.example.service.Action;
import org.example.service.YuYin;
import org.example.util.Property;
import org.example.view.ConfigFrame;
import org.example.view.CreateHotWordsFrame;
import org.example.view.MyFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Controller {


    private static final String SEPARATOR1 = "-------------------------------------------------------------------------------------------------------------------\n";
    MyFrame fm;
    private static final String SEPARATOR = "------------------------------------------------------------------------------------------------------------------------\n";
    File oldFile = new File("text");
    public static List<Vocab> vocabs;
    private Future<Vocab> task;
    List<JRadioButton> useVs = new ArrayList<>();
    //boolean vobNameChangeFlag = false;
    String oldName="";
    int[] changeFlag = new int[]{-1,-1};

    public Controller(MyFrame fm) {
        this.fm = fm;
    }

    public ActionListener fileRelatedListener = e -> {
        if (e.getSource() == fm.chooseBtn || e.getSource() == fm.selectItem) {
            int returnVal = fm.fileChooser.showOpenDialog(fm);        //是否打开文件选择
            if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
                fm.file = fm.fileChooser.getSelectedFile();
                fm.area.append(fm.file.getAbsolutePath() + "\n");
            }
        } else if (e.getSource() == fm.saveItem) {
            fm.fileSave.setSelectedFile(oldFile); // 设置选择器默认选中的文件
            fm.fileSave.showOpenDialog(fm);    // 打开对话框
            FileNameExtensionFilter filter = (FileNameExtensionFilter) fm.fileSave.getFileFilter(); // 拿到对话中选择的过滤器
            String[] extensions = filter.getExtensions();// 拿到过滤所允许的后缀
            String path = fm.fileSave.getCurrentDirectory().getPath();//获得保存路径文件夹
            String documentName;
            if (fm.fileSave.getSelectedFile() != null) {//获得保存文件名，如果没有，默认为text
                documentName = (oldFile = fm.fileSave.getSelectedFile()).getName();
            } else {
                documentName = "text";
            }
            boolean isLegal = false;
            for (String extension : extensions) {
                if (documentName.endsWith("." + extension)) isLegal = true;
            }
            if (!isLegal) {
                documentName = documentName + "." + extensions[0];
            }
            path = path + "\\" + documentName;//拼接成完整路径
            oldFile = new File(path);
            try {
                FileOutputStream fos = new FileOutputStream(oldFile, true);
                XWPFDocument docx = new XWPFDocument();
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                String text = fm.area.getText().substring(fm.area.getText().indexOf(SEPARATOR) + SEPARATOR.length());
                text = text.replaceAll("[ *\n*\r*]", "");
                StringBuilder sb = new StringBuilder(text);
                for (int i = 0; i < sb.length(); i++) {
                    if (sb.charAt(i) == '。') {
                        sb.insert(i + 1, '\n');
                    }
                }
                sb.insert(0, LocalDateTime.now().toString() + "\n");

                if (filter.getDescription().equals("txt")) {
                    osw.write(sb.toString());
                } else {
                    XWPFParagraph p = docx.createParagraph();
                    XWPFRun run = p.createRun();
                    run.setText(sb.toString());
                    docx.write(fos);
                }
                osw.flush();
                osw.close();
                docx.close();
                fos.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }


    };

    public ActionListener submitListener = e -> {
        if (fm.file != null) {
            Action action = new Action();
            fm.area.append("开始提交，请稍后...\n");
            Main.pool.submit(() -> {
                String useVocab = null;
                for (JRadioButton radioButton : useVs){
                    if (radioButton.isSelected()){
                        useVocab = radioButton.getActionCommand();
                        break;
                    }
                }
                action.compute(fm.file, useVocab);
                fm.area.append("提交完成\n");
                fm.area.append("任务id为：\n");
                Action.ids.forEach(id -> {
                    fm.area.append(id + "\n");
                });
            });
        } else {
            fm.area.append("尚未选择文件\n");
        }
    };

    public ActionListener getResListener = e -> {
        if (Action.ids.size() == 0) {
            fm.area.append("尚未提交任务\n");
            return;
        }
        fm.area.append("查询结果中，请稍后...\n" + SEPARATOR);
        Main.pool.submit(() -> {
            Action.ids.stream().forEach(id -> {
                try {
                    fm.sb.append(YuYin.INSTANCE.getResult(id));
                } catch (TencentCloudSDKException tencentCloudSDKException) {
                    fm.area.append("获取结果失败");
                }
            });
            Action.ids.clear();
            String res;
            fm.content = fm.area.getText() + (res = fm.sb.toString());
            String regex = "\\[.*]";
            if (!Property.propMap.get("TimeStamp").equals("1")) {
                fm.area.append(res);
            } else {
                fm.area.append(res.replaceAll(regex, ""));
            }
        });
    };

    public ActionListener clearPanelListener = e -> {
        fm.area.setText("");
        Action.ids.clear();
    };

    public ActionListener showConfigPanelListener = e -> {
        ConfigFrame cf = new ConfigFrame();
        Property.propMap.forEach((k, v) -> {
            Map<String, JRadioButton> map;
            if ((map = cf.config_select.get(k)) != null) {

                map.get(v).setSelected(true);
            }
        });
        JOptionPane.showMessageDialog(fm, cf, "配置", JOptionPane.PLAIN_MESSAGE);
        if (cf.defaultEffect_0.isSelected()) {
            Property.prop.setProperty("FilterModal", cf.FilterModal.getSelection().getActionCommand());
            Property.prop.setProperty("FilterDirty", cf.FilterDirty.getSelection().getActionCommand());
            try {
                Property.prop.store(new FileOutputStream(new File("config.properties")), "配置文件");
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } else {
            Property.propMap.put("FilterModal", cf.FilterModal.getSelection().getActionCommand());
            Property.propMap.put("FilterDirty", cf.FilterDirty.getSelection().getActionCommand());
        }


    };

    public ItemListener timeStampChangeListener = e -> {
        int timeStampVal = e.getStateChange() - 1;
        Property.propMap.put("TimeStamp", timeStampVal + "");
        Property.prop.setProperty("TimeStamp", timeStampVal + "");
        String regex = "\\[.+\\]";
        String s = null;
        if (timeStampVal == 1) {
            fm.area.setText(fm.area.getText().replaceAll(regex, ""));
        } else fm.area.setText(fm.content);
        try {
            Property.prop.store(new FileOutputStream("config.properties"), "config");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    };

    public ActionListener hotWordsPanelListener = e -> {
        CreateHotWordsFrame panel = new CreateHotWordsFrame();
        panel.panel1.setVisible(false);
        panel.input.addFocusListener(new MyFocusAdapter(panel));
        //initVocabs();
        crudVocabs(panel,0);
    };

    public ActionListener listVocabsListener = e -> {
        //initVocabs();
        fm.area.setText("");
        for (Vocab v : vocabs) {
            showVocab("", v);
        }
    };

    public ActionListener updateVocabsListener = e -> {
        actionPerformed(e, 1);
    };

    public ActionListener deleteVocabsListener = e -> {
        actionPerformed(e, 2);
    };

    public MenuListener initialVocabs = new MyMenuListener();


    private void actionPerformed(ActionEvent e, int flag) {
        CreateHotWordsFrame panel = new CreateHotWordsFrame();
        //initVocabs();
        Document dt = panel.id.getDocument();
        dt.addDocumentListener(new MyDocumentListener(panel));
        crudVocabs(panel, flag);
    }

    private void initVocabs() {
/*        try {

             Main.task.get();

        } catch (Exception interruptedException) {
            interruptedException.printStackTrace();
        }*/
    }

    private void crudVocabs(CreateHotWordsFrame panel, int flag) {
        try {
            if (task != null){
                task.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String vocabId = null, vocabName = null, vocabDesc = null, vocabContent = null;
        boolean isDefault = false;
        boolean isValid = false;
        boolean isCreate = true;
        while (!isValid) {
            if (JOptionPane.showConfirmDialog(fm, panel, "新建热词表", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == 0) {
                vocabId = panel.id.getText();
                vocabName = panel.name.getText();
                vocabDesc = panel.describe.getText();
                vocabContent = panel.input.getText();
                isDefault = panel.isDefault.isSelected() ? true : false;
                if (vocabs.size() == 0) isCreate = true;
                if (flag == 0){
                    for (Vocab v : vocabs) {
                        if (v.getName().equals(vocabName)) {
                            isCreate = false;
                            panel.name.setText("名称重复");
                            break;
                        }
                    }
                }
                if (isCreate) {
                    try {
                        switch (flag) {
                            case 0:
                                CreateAsrVocabResponse response = YuYin.INSTANCE.createHotWords(vocabName, vocabDesc, vocabContent, isDefault);
                                if (!response.equals("")){
                                    isValid = true;
                                    changeFlag[0] = 0;
                                    changeFlag[1] = vocabs.size();
                                    task = Main.pool.submit(() -> {
                                        Vocab v;
                                        vocabs.add((v = YuYin.INSTANCE.getVocabById(response.getVocabId())));
                                        return v;
                                    });
                                    showVocab("单词表添加成功：\n" , (Vocab) task.get());
                                }
                                break;
                            case 1:
                                UpdateAsrVocabResponse resp = YuYin.INSTANCE.updateVocabs(vocabId,vocabName,vocabDesc,vocabContent,isDefault);
                                if (!resp.equals("")){
                                    isValid = true;
                                    if (!oldName.equals(vocabName)){
                                        for (int i = 0; i < vocabs.size(); i++){
                                            if (vocabs.get(i).getVocabId().equals(vocabId)){
                                                changeFlag[0] = 1;
                                                changeFlag[1] = i;
                                            }
                                        }
                                    }
                                    task = Main.pool.submit(() -> {
                                        Vocab vocab = YuYin.INSTANCE.getVocabById(resp.getVocabId());
                                        for (int i = 0; i < vocabs.size(); i++){
                                            if (vocabs.get(i).getVocabId().equals(vocab.getVocabId())){
                                                vocabs.set(i, vocab);
                                            }
                                        }
                                        return vocab;
                                    });
                                    fm.area.setText("");
                                    showVocab("单词表更新成功：\n" , (Vocab) task.get());
                                }
                                break;
                            case 2:
                                DeleteAsrVocabResponse respo = YuYin.INSTANCE.deleteVocabs(vocabId);
                                if (!respo.equals("")){
                                    isValid = true;
                                    for (int i = 0; i < vocabs.size(); i++){
                                        if (vocabs.get(i).getVocabId().equals(vocabId)){
                                            changeFlag[0] = 2;
                                            changeFlag[1] = i;
                                        }
                                    }
                                    String finalVocabId = vocabId;
                                    task = Main.pool.submit(() -> {
                                        for (int i = 0; i < vocabs.size(); i++){
                                            if (vocabs.get(i).getVocabId().equals(finalVocabId)){
                                                vocabs.remove(i);
                                                break;
                                            }
                                        }
                                        return null;
                                    });
                                    fm.area.setText("");
                                    showVocab("单词表删除成功\n" , null);
                                }

                        }


                    } catch (TencentCloudSDKException tencentCloudSDKException) {
                        panel.input.append("格式有误(id/词组)");
                        isValid = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                isValid = true;
            }
        }
    }


    class MyDocumentListener implements DocumentListener {
        CreateHotWordsFrame panel;

        MyDocumentListener(CreateHotWordsFrame panel) {
            super();
            this.panel = panel;
        }

        public void insertUpdate(DocumentEvent e) {
            updateTextArea(e);
        }

        public void removeUpdate(DocumentEvent e) {
            updateTextArea(e);
        }


        public void changedUpdate(DocumentEvent e) {
            updateTextArea(e);
        }

        void updateTextArea(DocumentEvent e) {
            try {
                String id = e.getDocument().getText(0, e.getDocument().getLength());
                vocabs.forEach(v -> {
                    if (v.getVocabId().equals(id)) {
                        oldName = v.getName();
                        panel.name.setText(v.getName());
                        panel.describe.setText(v.getDescription());
                        panel.input.setText("");
                        for (HotWord h : v.getWordWeights()) {
                            panel.input.append(h.getWord() + "|" + h.getWeight() + "\n");
                        }
                        panel.isDefault.setSelected(v.getState() == 1 ? true : false);
                    }

                });
            } catch (BadLocationException badLocationException) {
                badLocationException.printStackTrace();
            }
        }
    }

    class MyFocusAdapter extends FocusAdapter {
        CreateHotWordsFrame panel;

        MyFocusAdapter(CreateHotWordsFrame panel) {
            this.panel = panel;
        }

        @Override
        public void focusGained(FocusEvent e) {
            panel.input.setText(panel.content.toString());
            panel.input.setFont(null);
        }

        @Override
        public void focusLost(FocusEvent e) {
            panel.content.delete(0, panel.content.length());
            panel.content.append(panel.input.getText());
            if (panel.input.getText().length() == 0) {
                panel.input.setText("示例：敏捷编程|5（热词|权重）");
            }
        }
    }

    class MyMenuListener implements MenuListener {

        @Override
        public void menuSelected(MenuEvent e) {
            try {
                if (vocabs == null || vocabs.size() == 0){
                    vocabs = (List<Vocab>) Main.task.get();
                    for (Vocab v : vocabs){
                        JRadioButton useV = new JRadioButton(v.getName());
                        useV.setActionCommand(v.getVocabId());
                        useVs.add(useV);
                        fm.useHotWords.add(useV);
                    }
                }else {
                    if (task != null){
                        task.get();
                    }
                    switch (changeFlag[0]){
                        case 0:
                            Vocab v = vocabs.get(changeFlag[1]);
                            JRadioButton useV = new JRadioButton(v.getName());
                            useV.setActionCommand(v.getVocabId());
                            useVs.add(useV);
                            fm.useHotWords.add(useV);
                            break;
                        case 1:
                            Vocab vo = vocabs.get(changeFlag[1]);
                            JRadioButton radioButton = (JRadioButton)fm.useHotWords.getMenuComponent(changeFlag[1]);
                            radioButton.setText(vo.getName());
                            break;
                        case 2:
                            fm.useHotWords.remove(changeFlag[1]);
                            break;
                    }
                    changeFlag[0] = -1;
                    changeFlag[1] = -1;
                    /*if (vocabs.size() != useVs.size()){
                    useVs.clear();
                    fm.useHotWords.removeAll();
                    for (Vocab v : vocabs){
                        JRadioButton useV = new JRadioButton(v.getName());
                        useV.setActionCommand(v.getVocabId());
                        useVs.add(useV);
                        fm.useHotWords.add(useV);
                    }
                }else if (vobNameChangeFlag){
                    useVs.clear();
                    fm.useHotWords.removeAll();
                    for (Vocab v : vocabs){
                        JRadioButton useV = new JRadioButton(v.getName());
                        useV.setActionCommand(v.getVocabId());
                        useVs.add(useV);
                        fm.useHotWords.add(useV);
                    }
                }*/
                }

            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
            }
        }

        @Override
        public void menuDeselected(MenuEvent e) {

        }

        @Override
        public void menuCanceled(MenuEvent e) {

        }
    }

    private void showVocab(String msg, Vocab v) {
        fm.area.append(msg);
        if(v != null){
            fm.area.append(
                    "名称：" + v.getName() + "\n" +
                            "描述：" + v.getDescription() + "\n" +
                            "id：" + v.getVocabId() + "\n"
            );
            if (v.getState() == 1) fm.area.append("默认\n");
            fm.area.append("\n");
            for (HotWord h : v.getWordWeights()) {
                fm.area.append(h.getWord() + "|" + h.getWeight() + "\n");
            }
            fm.area.append(SEPARATOR1);
            fm.area.append("\n");
        }
    }
}