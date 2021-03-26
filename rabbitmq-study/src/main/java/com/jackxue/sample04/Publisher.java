package com.jackxue.sample04;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class Publisher {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        //声明直接交换机
        channel.exchangeDeclare("logs_direct", "direct");

        //发布 info debug error 得消息
        channel.basicPublish("logs_direct", "debug", null, "debug message".getBytes());
        channel.basicPublish("logs_direct", "info", null, "info message".getBytes());
        channel.basicPublish("logs_direct", "error", null, "error message".getBytes());

        //释放资源
        RabbitMqUtils.close(channel, connection);
    }
}
