package com.myGenerator.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesUtil {
    public static Properties properties = new Properties();
    public static Map<String, String> propMap = new ConcurrentHashMap<>();

    // 加载配置文件 并且将配置文件中的值以key与value的形式存在propMap中 方便拿取
    static {
        InputStream is = null;

        is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");

//        is = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(is);

            properties.keySet().forEach((k) -> propMap.put(k.toString(), properties.getProperty(k.toString())));

        } catch (IOException e) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {

            }
        }
    }

    public static String getString(String key) {
        return propMap.get(key);
    }

}
