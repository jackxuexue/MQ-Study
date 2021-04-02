package com.jackxue.mq;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutModule {

    @RabbitListener(bindings = @QueueBinding(value = @Queue,
                    exchange = @Exchange(value = "logs", type = "fanout")))
    public void consumer1(String message){
        System.out.println("consumer1:" + message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue,
            exchange = @Exchange(value = "logs", type = "fanout")))
    public void consumer2(String message){
        System.out.println("consumer2:" + message);
    }
}
