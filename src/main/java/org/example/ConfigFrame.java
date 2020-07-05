package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigFrame extends JPanel {



    JPanel modalPanel = new JPanel();
    JLabel modalLabel = new JLabel("语气词过滤:");
    JRadioButton modal_0 = new JRadioButton("不过滤");
    JRadioButton modal_1 = new JRadioButton("部分");
    JRadioButton modal_2 = new JRadioButton("严格");
    ButtonGroup FilterModal = new ButtonGroup(){{
        add(modal_0);
        add(modal_1);
        add(modal_2);
    }};

    JPanel dirtyPanel = new JPanel();
    JLabel dirtyLabel = new JLabel("脏词过滤:");
    JRadioButton dirty_0 = new JRadioButton("不过滤");
    JRadioButton dirty_1 = new JRadioButton("过滤");
    JRadioButton dirty_2 = new JRadioButton("替换");
    ButtonGroup FilterDirty = new ButtonGroup(){{
        add(dirty_0);
        add(dirty_1);
        add(dirty_2);
    }};


    JPanel defaultEffectPanel = new JPanel();
    JCheckBox defaultEffect_0 = new JCheckBox("默认生效");



    public Map<String, Map> config_select = new HashMap<String, Map>(){{
        put("FilterModal",new HashMap<String, JRadioButton>(){{
            put("0", modal_0);
            put("1", modal_1);
            put("2", modal_2);
        }});
        put("FilterDirty",new HashMap<String, JRadioButton>(){{
            put("0", dirty_0);
            put("1", dirty_1);
            put("2", dirty_2);
        }});

    }};



    public ConfigFrame(){
        //this.setPreferredSize(new Dimension(300,200));
        this.setLayout(new GridLayout(4,1));
        modal_0.setActionCommand("0");
        modal_1.setActionCommand("1");
        modal_2.setActionCommand("2");

        dirty_0.setActionCommand("0");
        dirty_1.setActionCommand("1");
        dirty_2.setActionCommand("2");

        modalPanel.add(modalLabel);
        modalPanel.add(modal_0);
        modalPanel.add(modal_1);
        modalPanel.add(modal_2);
        dirtyPanel.add(dirtyLabel);
        dirtyPanel.add(dirty_0);
        dirtyPanel.add(dirty_1);
        dirtyPanel.add(dirty_2);
        defaultEffectPanel.add(defaultEffect_0);

        this.add(modalPanel);
        this.add(dirtyPanel);
        this.add(defaultEffectPanel);
    }

}
