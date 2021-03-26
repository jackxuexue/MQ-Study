package com.jackxue.sample06;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.*;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;

import java.io.IOException;

public class RPCServer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        //声明rpc 队列
        channel.queueDeclare("rpc_queue", false, false, false, null);
        //不知道啥意思
        channel.queuePurge("rpc_queue");
        //一次只消费一条消息
        channel.basicQos(1);

        //等待客户端消息回调
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String input = new String(message.getBody(), "UTF-8");
                System.out.println(input);
                int num = Integer.parseInt(input);
                double data = getSqrt(num);  //求算术平方根
                //接收到CorrelationId去构建参数，并且拿到客户端指定的结果返回队列
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(message.getProperties().getCorrelationId())
                        .build();
                //往客户端声明的queue去发送结果
                channel.basicPublish("", message.getProperties().getReplyTo(), replyProps, (""+data).getBytes());
                //确认消息
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        channel.basicConsume("rpc_queue", false, deliverCallback, x->{});

    }

    public static double getSqrt(int number){
        return Math.sqrt(number);
    }
}
