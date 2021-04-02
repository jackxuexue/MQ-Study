package com.jackxue.mq;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RouterModule {

    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(value = "logs_direct", type = "direct"),
                    key = {"info", "debug", "error"}))
    public void consumer1(String message){
        System.out.println("消费者1：" + message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(value = "logs_direct", type = "direct"),
            key = {"error"}))
    public void consumer2(String message){
        System.out.println("消费者2：" + message);
    }
}
