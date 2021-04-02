package com.jackxue.mq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello", durable = "false", autoDelete = "true"))
public class HelloWorld {

    @RabbitHandler
    public void handleMessage(String message){
        System.out.println(message);
    }
}
