package org.example;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Property {
    public static Map<String, String> propMap = new HashMap<>();
    public static Properties prop;
    static {
        prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(getPath()+"\\config.properties");
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }

        prop.forEach((k,v) -> {
            propMap.put((String) k,(String) v);
        });
    }

    public static String getPath()
    {
        String path = Property.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if(System.getProperty("os.name").contains("dows"))
        {
            path = path.substring(1,path.length());
        }
        if(path.contains("jar"))
        {
            path = path.substring(0,path.lastIndexOf("."));
            return path.substring(0,path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }
}
