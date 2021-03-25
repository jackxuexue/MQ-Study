package com.jackxue.sample02;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class Worker {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();
        String queueName = "durableTask";
        channel.queueDeclare(queueName, true, false, false, null);

        DeliverCallback deliverCallback = new DeliverCallback() {
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println(consumerTag + " worker " +  new String(message.getBody(), "UTF-8"));
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, x->{});
    }
}
