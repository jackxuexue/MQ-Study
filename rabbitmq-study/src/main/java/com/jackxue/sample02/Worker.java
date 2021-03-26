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
        //生命队列
        channel.queueDeclare(queueName, true, false, false, null);

        channel.basicQos(1); //一次拿一个消息
        DeliverCallback deliverCallback = new DeliverCallback() {
            public void handle(String consumerTag, Delivery message) throws IOException {
                System.out.println(consumerTag + " worker " +  new String(message.getBody(), "UTF-8"));
                //手动确认消息已经被消费，才会从队列中删除
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        //不要自动确认消息
        channel.basicConsume(queueName, false, deliverCallback, x->{});
    }
}
