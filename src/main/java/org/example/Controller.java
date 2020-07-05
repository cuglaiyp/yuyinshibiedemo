package org.example;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.Map;

public class Controller {
    MyFrame fm;
    static final String SEPARATOR = "------------------------------------------------------------------------------------------------------------------------\n";
    File oldFile = new File("text");

    Controller(MyFrame fm){
        this.fm = fm;
    }
    public ActionListener fileRelatedListener = e -> {
        if(e.getSource() == fm.chooseBtn || e.getSource() == fm.selectItem){
            int returnVal = fm.fileChooser.showOpenDialog(fm);        //是否打开文件选择
            if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型
                fm.file = fm.fileChooser.getSelectedFile();
                fm.area.append(fm.file.getAbsolutePath() + "\n");
            }
        }else if (e.getSource() == fm.saveItem){
            fm.fileSave.setSelectedFile(oldFile); // 设置选择器默认选中的文件
            fm.fileSave.showOpenDialog(fm);    // 打开对话框
            FileNameExtensionFilter filter = (FileNameExtensionFilter) fm.fileSave.getFileFilter(); // 拿到对话中选择的过滤器
            String[] extensions = filter.getExtensions();// 拿到过滤所允许的后缀
            String path = fm.fileSave.getCurrentDirectory().getPath();//获得保存路径文件夹
            String documentName;
            if(fm.fileSave.getSelectedFile()!=null){//获得保存文件名，如果没有，默认为text
                documentName = (oldFile = fm.fileSave.getSelectedFile()).getName();
            }else{
                documentName = "text";
            }
            boolean isLegal = false;
            for(String extension : extensions){
                if(documentName.endsWith("."+extension)) isLegal = true;
            }
            if (!isLegal){
                documentName = documentName + "." + extensions[0];
            }
            path = path+"\\"+documentName;//拼接成完整路径
            oldFile = new File(path);
            try {
                FileOutputStream fos = new FileOutputStream(oldFile,true);
                XWPFDocument docx = new XWPFDocument();
                OutputStreamWriter osw = new OutputStreamWriter(fos);
                String text = fm.area.getText().substring(fm.area.getText().indexOf(SEPARATOR) + SEPARATOR.length());
                text = text.replaceAll("[ *\n*\r*]", "");
                StringBuilder sb = new StringBuilder(text);
                for (int i = 0; i < sb.length(); i++){
                    if (sb.charAt(i) == '。'){
                        sb.insert(i+1, '\n');
                    }
                }
                sb.insert(0, LocalDate.now().toString()+"\n");

                if (filter.getDescription().equals("txt")){
                    osw.write(sb.toString());
                }else {
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
            new Thread(() -> {
                action.compute(fm.file);
                fm.area.append("提交完成\n");
                fm.area.append("任务id为：\n");
                Action.ids.forEach(id -> {
                    fm.area.append(id + "\n");
                });
            }).start();
        } else {
            fm.area.append("尚未选择文件\n");
        }
    };


    public ActionListener getResListener = e -> {
        if (Action.ids.size() == 0) {
            fm.area.append("尚未提交任务\n");
            return;
        }
        fm.area.append("查询结果中，请稍后...\n"+ SEPARATOR);
        new Thread(() -> {
            Action.ids.parallelStream().forEachOrdered(id -> {
                fm.sb.append(YuYinUtil.INSTANCE.getResult(id));
            });
            //Action.ids.clear();
            String res;
            fm.content = fm.area.getText() + (res = fm.sb.toString());
            String regex = "\\[.*\\]";
            if(!Property.propMap.get("TimeStamp").equals("1")){
                fm.area.append(res);
            }else{
                fm.area.append(res.replaceAll(regex,""));
            }
        }).start();
    };


    public ActionListener clearPanelListener = e -> {
        fm.area.setText("");
        Action.ids.clear();
    };

    public ActionListener showConfigPanelListener = e -> {
        ConfigFrame cf = new ConfigFrame();
        Property.propMap.forEach((k,v) -> {
            Map<String,JRadioButton> map = null;
            if((map = cf.config_select.get(k)) != null){

                map.get(v).setSelected(true);
    }
        });
        JOptionPane.showMessageDialog(fm, cf ,"配置", JOptionPane.DEFAULT_OPTION);
        if(cf.defaultEffect_0.isSelected()){
            Property.prop.setProperty("FilterModal",cf.FilterModal.getSelection().getActionCommand());
            Property.prop.setProperty("FilterDirty",cf.FilterDirty.getSelection().getActionCommand());
            try {
                Property.prop.store(new FileOutputStream(new File("config.properties")),"配置文件");
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }else{
            Property.propMap.put("FilterModal",cf.FilterModal.getSelection().getActionCommand());
            Property.propMap.put("FilterDirty",cf.FilterDirty.getSelection().getActionCommand());
        }


    };

    ItemListener timeStampChangeListener = e -> {
        int timeStampVal = e.getStateChange() - 1;
        Property.propMap.put("TimeStamp", timeStampVal+"");
        Property.prop.setProperty("TimeStamp", timeStampVal+"");
        String regex = "\\[.+\\]";
        String s = null;
        if(timeStampVal == 1){
            fm.area.setText(fm.area.getText().replaceAll(regex,""));
        }else fm.area.setText(fm.content);
        try {
            Property.prop.store(new FileOutputStream("config.properties"), "config");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    };
}
