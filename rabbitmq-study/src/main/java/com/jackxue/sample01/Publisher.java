package com.jackxue.sample01;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitMqUtils.getConnection();

        Channel channel = connection.createChannel();

        String queueName = "hello";
        channel.queueDeclare(queueName, false, false, false, null);

        long currentTime = System.currentTimeMillis();
        int i = 0;
        for (i = 0; i < 10; i++) {
            String message = i + ": hello!";
            channel.basicPublish("", queueName, null, message.getBytes());
        }
        System.out.println("send " + i +" message need " + (System.currentTimeMillis()-currentTime) + " ms");

        RabbitMqUtils.close(channel, connection);
    }
}
