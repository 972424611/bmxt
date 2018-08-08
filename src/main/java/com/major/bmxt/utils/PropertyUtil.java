package com.major.bmxt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取.properties信息
 * @author aekc
 */
public class PropertyUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties properties;

    private static Properties teamProperties;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        properties = new Properties();
        teamProperties = new Properties();
        InputStream in = null;
        try {
            //通过类加载器进行获取properties文件流
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("resource/resource.properties");
            properties.load(in);
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("resource/team.properties");
            //当properties文件中含有中文key或者中文value的时候先包装为InputStreamReader, 才不会乱码.
            teamProperties.load(new InputStreamReader(in, "UTF-8"));
        } catch (FileNotFoundException e) {
            logger.error("resource.properties文件没有找到");
        } catch (IOException e) {
            logger.error("加载文件出现错误");
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("resource.properties文件流关闭出现异常");
            }
        }
    }

    public static String getTeamProperty(String key) {
        if(teamProperties == null) {
            loadProps();
        }
        return teamProperties.getProperty(key);
    }

    public static String getProperty(String key) {
        if(properties == null) {
            loadProps();
        }
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == properties) {
            loadProps();
        }
        return properties.getProperty(key, defaultValue);
    }
}
