package com.phonepe.queue.utils;

import com.phonepe.queue.Consumer;
import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.MessageType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FailConsumer extends Consumer {

    public FailConsumer(List<MessageType> messageTypes, List<Consumer> dependentConsumers) {
        super(messageTypes, dependentConsumers);
    }

    @Override
    public boolean consume(Message message) {
        assertEquals(1, 2);
        return false;
    }

}
