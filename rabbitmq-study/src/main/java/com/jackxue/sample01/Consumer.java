package com.jackxue.sample01;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();

        Channel channel = connection.createChannel();

        String queueName = "hello";

        channel.basicConsume(queueName, true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("consumerTag:{} envelop:{}  body:{}", consumerTag, envelope, new String(body, "UTF-8"));
            }
        });

    }
}
