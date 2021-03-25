package com.jackxue.sample03;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class ReceiveLogs1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare("logs", "fanout");
        //获取系统分配的队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定queue和交换机
        channel.queueBind(queueName, "logs", "");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println("消费者2：" + new String(message.getBody(), "UTF-8"));
            }
        };

        channel.basicConsume(queueName, true, deliverCallback, x->{});
    }
}
