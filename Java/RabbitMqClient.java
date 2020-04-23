package com.globalegrow.bigdata.utils;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.TimeoutException;

/**
 * @author 小和尚
 * @version 1.0.0
 * @ClassName gb_email_browse.com.globalegrow.bigdata.utils.RabbitMqClient
 * @Description TODO
 * @createTime 2020年04月20日 11:02:00
 */
public class RabbitMqClient implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(RabbitMqClient.class);
    private static Connection connection;
    private static Channel channel;
    private static String exchange_name;
    private static String queue_name;
    private static String route_key;


    static {
        exchange_name = PropertyUtil.getProperty("exchange-name");
        queue_name = PropertyUtil.getProperty("queue-name");
        route_key = PropertyUtil.getProperty("route-key");
        connection = initConnect();
        channel = bindAndReturnChannel();

    }

/**
 * @title initConnect
 * @description 初始化Connect
 * @author 小和尚
 * @param
 * @updateTime 2020/4/23 10:01
 * @return Connection
 * @throws TimeoutException IOException
 */
    private static Connection initConnect() {

        String host = PropertyUtil.getProperty("host");
        String port = PropertyUtil.getProperty("port");
        String userName = PropertyUtil.getProperty("userName");
        String passWord = PropertyUtil.getProperty("passWord");
        String virtual_host = PropertyUtil.getProperty("virtual-host");
        ConnectionFactory factory = new ConnectionFactory();
        // 连接IP
        factory.setHost(host);
        // 连接端口
        factory.setPort(Integer.valueOf(port));
        // 虚拟机
        factory.setVirtualHost(virtual_host);
        // 用户
        factory.setUsername(userName);
        factory.setPassword(passWord);

        // 建立连接
        Connection conn = null;
        try {
            conn = factory.newConnection();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * @title bindAndReturnChannel
     * @description 绑定channel
     * @author 小和尚
     * @param
     * @updateTime 2020/4/23 10:02
     * @return Channel
     * @throws IOException
     */
    private static Channel bindAndReturnChannel() {

        if (connection != null) {
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                connection = getConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                channel = connection.createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            channel.exchangeDeclare(exchange_name, "direct", true);
            channel.queueDeclare(queue_name, true, false, false, null);
            channel.queueBind(queue_name, exchange_name, route_key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return channel;
    }

    /**
     * @title getConnection
     * @description Connection初始化失败时 会调用该方法进行重试 每次间隔3s
     * @author 小和尚
     * @param
     * @updateTime 2020/4/23 10:03
     * @return Connection
     * @throws Exception
     */
    public static Connection getConnection() throws Exception {
        if (connection != null) {
            return connection;
        } else {
            int retryTimes = 3;
            while (retryTimes > 0) {

                connection = initConnect();
                if (connection != null) {
                    return connection;
                } else {
                    retryTimes--;
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (connection == null) {
                throw new Exception("rabbitMQ连接失败");
            }
            return connection;
        }
    }

    /**
     * @title publishRecords
     * @description 推送数据到rabbitMq
     * @author 小和尚
     * @param
     * @updateTime 2020/4/23 10:03
     * @return
     * @throws
     */
    public static void publishRecords(byte[] records) throws Exception {

        channel.basicPublish(exchange_name, route_key, null, records);

    }

/**
 * @title close
 * @description 关闭connection channel
 * @author 小和尚
 * @param
 * @updateTime 2020/4/23 10:06
 * @return
 * @throws
 */
    public static void close() throws Exception {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}