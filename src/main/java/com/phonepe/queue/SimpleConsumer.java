package com.phonepe.queue;

import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.MessageType;

import java.util.List;

public class SimpleConsumer extends Consumer {

    public SimpleConsumer(List<MessageType> messageTypes, List<Consumer> dependentConsumers) {
        super(messageTypes, dependentConsumers);
    }

    @Override
    public boolean consume(Message message) {
        System.out.println(message);
        return true;
    }
}
