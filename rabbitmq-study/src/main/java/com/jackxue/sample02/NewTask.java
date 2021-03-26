package com.jackxue.sample02;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewTask {
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "durableTask";
        channel.queueDeclare(queueName, true, false, false, null);

        for(int i=0; i<60; i++) {
            String message = i + ":new task!";
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        }

        RabbitMqUtils.close(channel, connection);
    }
}
