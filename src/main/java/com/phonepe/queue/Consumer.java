package com.phonepe.queue;

import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.MessageType;
import com.phonepe.queue.queue.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Consumer {

    private final List<MessageType> consumerMessageTypes;

    private final List<Consumer> dependentConsumers;

    public Consumer(List<MessageType> messageTypes, List<Consumer> dependentConsumers){
        this.dependentConsumers = Objects.requireNonNullElseGet(dependentConsumers, ArrayList::new);
        this.consumerMessageTypes = messageTypes;
    }

    public boolean subscribe(List<Queue> queues) {
        queues.forEach(q -> q.subscribe(this));
        return true;
    }

    public abstract boolean consume(Message message);

    public List<MessageType> getConsumerMessageTypes() {
        return consumerMessageTypes;
    }
}
