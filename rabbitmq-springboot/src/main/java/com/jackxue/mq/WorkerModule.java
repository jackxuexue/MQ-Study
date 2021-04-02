package com.jackxue.mq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkerModule {

    @RabbitListener(queuesToDeclare = @Queue(value = "worker", durable = "false", autoDelete = "true"))
    public void worker1(String message){
        System.out.println("worker1: " + message);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "worker", durable = "false", autoDelete = "true"))
    public void worker2(String message){
        System.out.println("worker2: " + message);
    }
}
