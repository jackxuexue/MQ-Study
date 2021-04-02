package com.jackxue.sample07;

import com.jackxue.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BooleanSupplier;

public class PublisherConfirm {
    static final int MESSAGE_COUNT = 50_000;

    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        //声明queue
        String queueName = "publisher_confirm";
        channel.queueDeclare(queueName, false, false, true, null);

        //开启消息确认
        channel.confirmSelect();

        ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //已发送的消息，从map中清楚
        ConfirmCallback confirmCallback = (seqNum, multiple)->{
            if(multiple){
//                System.out.println("send 1 " + seqNum + " " + outstandingConfirms.size());
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(
                        seqNum, true
                );
                confirmed.clear();
            }else {
//                System.out.println("send 2" + seqNum + " " + outstandingConfirms.size());
                outstandingConfirms.remove(seqNum);
            }
        };

        channel.addConfirmListener(confirmCallback, (seqNum, multiple)->{
            String body = outstandingConfirms.get(seqNum);
            System.err.format(
                    "Message with body %s has been nack-ed. Sequence number: %d, multiple: %b%n",
                    body, seqNum, multiple
            );
            confirmCallback.handle(seqNum, multiple);
        });

        long start = System.nanoTime();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String body = String.valueOf(i);
            outstandingConfirms.put(channel.getNextPublishSeqNo(), body);
            channel.basicPublish("", queueName, null, body.getBytes());
        }

        System.out.println("已经发布的消息数量：" + MESSAGE_COUNT);
        if (!waitUntil(Duration.ofSeconds(10), () -> outstandingConfirms.isEmpty())) {

            throw new IllegalStateException("All messages could not be confirmed in 10 seconds");
        }

        long end = System.nanoTime();
        System.out.format("Published %,d messages and handled confirms asynchronously in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());

        RabbitMqUtils.close(channel, connection);

    }

    static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
        int waited = 0;
        while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
            Thread.sleep(100L);
            waited = +100;
        }
        System.out.println(condition.getAsBoolean());
        return condition.getAsBoolean();
    }
}
