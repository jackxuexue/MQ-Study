package com.jackxue.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RPCModule {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queuesToDeclare = {@Queue("rpc")})
    public void rpcServer(Message message){
        MessageProperties mp = new MessageProperties();
        mp.setCorrelationId(message.getMessageProperties().getCorrelationId());
        Message reply = new Message("hello".getBytes(), mp);
        System.out.println(message.getMessageProperties().getReplyTo());
        rabbitTemplate.send("", message.getMessageProperties().getReplyTo(), reply);
    }
}
