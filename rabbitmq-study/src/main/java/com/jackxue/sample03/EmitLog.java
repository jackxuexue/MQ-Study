package com.jackxue.sample03;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class EmitLog {
    public static void main(String[] args) throws IOException {
        //获取连接对象
        Connection connection = RabbitMqUtils.getConnection();
        //获取channel
        Channel channel = connection.createChannel();
        //获取系统分配的临时队列
        String queueName = channel.queueDeclare().getQueue();
        //声明交换机
        channel.exchangeDeclare("logs", "fanout");
        //绑定队列
        channel.queueBind(queueName, "logs", "");

        //发布消息
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("logs", "",  null, (i+":logs").getBytes());
        }

        //释放资源
        RabbitMqUtils.close(channel, connection);
    }
}
