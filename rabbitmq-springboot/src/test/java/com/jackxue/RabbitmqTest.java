package com.jackxue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@SpringBootTest(classes = MainClass.class)
@RunWith(SpringRunner.class)
public class RabbitmqTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试RPC调用
     *
     */
    @Test
    public void testRPC(){
        MessageProperties mp = new MessageProperties();
        mp.setCorrelationId(UUID.randomUUID().toString());
        Message message = new Message("你好".getBytes(), mp);
        Message rpc = rabbitTemplate.sendAndReceive("", "rpc", message);
        System.out.println("获得消息：" + new String(rpc.getBody()));
    }
    /**
     * 测试topic 消息
     */
    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("topics", "order.add", "order.add 消息！");
    }
    /**
     * 测试 direct路由模型
     */
    @Test
    public void testRouter(){
        rabbitTemplate.convertAndSend("logs_direct", "info", "direct 路由的 info key消息");
        rabbitTemplate.convertAndSend("logs_direct", "error", "direct 路由的 error key消息");
    }
    /**
     * 测试fanout 模型
     */
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("logs", "", "hello fanout!");
    }
    /**
     * 测试 Worker模型
     */
    @Test
    public void testWorker(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("worker", i+ ": hello worker!" );
        }
    }
    /**
     * 测试Hello World 模型
     */
    @Test
    public void testHelloWorld(){
        rabbitTemplate.convertAndSend("hello", "hello MQ!");
    }
}
