package com.phonepe.queue.utils;

import com.phonepe.queue.Consumer;
import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.MessageType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertConsumer2 extends Consumer {

    public AssertConsumer2(List<MessageType> messageTypes, List<Consumer> dependentConsumers) {
        super(messageTypes, dependentConsumers);
    }

    @Override
    public boolean consume(Message message) {
        assertTrue(getConsumerMessageTypes().contains(message.getMessageType())
                || getConsumerMessageTypes().contains(MessageType.ALL));
        assertEquals(message.getVal(), "{}");
        return true;
    }
}
