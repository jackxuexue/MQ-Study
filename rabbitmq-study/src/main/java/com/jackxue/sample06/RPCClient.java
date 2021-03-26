package com.jackxue.sample06;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RPCClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare("rpc_queue", false, false, false, null);
        final String corrId = UUID.randomUUID().toString();

        //定义一个系统分配的接收回调结果的queue
        String queueCb = channel.queueDeclare().getQueue();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .replyTo(queueCb)
                .correlationId(corrId)  //这个时每次都构建一个独有的id
                .build();

        //发布远程调用，求4的平方根
        channel.basicPublish("", "rpc_queue", properties, "4".getBytes());

        //等待消息回调
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                blockingQueue.offer(new String(message.getBody(), "UTF-8"));
            }
        };
        channel.basicConsume(queueCb, true, deliverCallback, x->{});

        String take = blockingQueue.take();
        System.out.println("logx=4 x=" + take);

        RabbitMqUtils.close(channel, connection);

    }
}
