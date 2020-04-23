package com.globalegrow.bigdata.utils;

import java.io.*;
import java.util.Date;
import java.util.Properties;

/**
 * @author 小和尚
 * @version 1.0.0
 * @ClassName gb_email_browse.PropertyUtil
 * @Description TODO
 * @createTime 2020年04月20日 11:20:00
 */
public class PropertyUtil {
    private static Properties props;
    private  String propsName;
    static{
        loadProps();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream inputStream=null;
        try {
            inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("myRabbitMQ_test.properties");
            props.load(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("myRabbitMQ_test.properties文件未找到");
        } catch (IOException e) {
            System.out.println("出现IOException");
        } finally {
            try {
                if(null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                System.out.println("config.properties文件流关闭出现异常");
            }
        }
        System.out.println("加载properties文件内容完成...........");
    }

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    public static void setProperty(String key,String value) {
        try {
            if(null == props) {
                loadProps();
            }
            FileOutputStream oFile = new FileOutputStream(new File("myRabbitMQ_test.properties"));
            props.setProperty(key, value);
            props.store(oFile, new Date().toString());
            oFile.close();
        } catch (IOException e) {

            e.printStackTrace();
        }


    }
}
