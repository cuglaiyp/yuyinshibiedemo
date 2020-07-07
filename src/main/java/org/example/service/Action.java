package org.example.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Action {
    public final static int STANDARD_SIZE = 5 * 1024 * 1024;
    public static List<Long> ids = new ArrayList<Long>()/*{{
        add((long) 820690105);
        add((long) 820690181);
        add((long) 820690227);
    }}*/;

    public void compute(File file, String useVocab) {
        int len = 0;
        List<byte[]> buffers = new ArrayList<>();
        byte[] buffer = new byte[STANDARD_SIZE];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                buffers.add(Arrays.copyOfRange(buffer, 0, buffer.length));
            }
            for (int i = 0; i < buffers.size(); i++) {
                ids.add(YuYin.INSTANCE.requestServ(buffers.get(i), useVocab).getData().getTaskId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


