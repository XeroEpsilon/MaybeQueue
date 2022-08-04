package com.phonepe.queue.queue;

import com.phonepe.queue.Consumer;
import com.phonepe.queue.order.OrderResolver;

import java.util.ArrayList;

public class Queue {

    private final int maxSize;
    // TODO
    private final ArrayList<Message> queue = new ArrayList<>();
    private final ArrayList<Consumer> consumers = new ArrayList<>();

    private final OrderResolver orderResolver;

    public Queue(int maxSize, OrderResolver orderResolver) {
        this.maxSize = maxSize;
        this.orderResolver = orderResolver;
    }

    // TODO: JSON Only
    public void pushMessage(Message message) throws Exception {
        synchronized (queue) {
            if (queue.size() >= maxSize){
                throw new Exception("max size");
            }
            queue.add(message);
            // should be async?
            for (Consumer consumer : consumers) {
                if (consumer.getConsumerMessageTypes().contains(message.getMessageType())
                        || consumer.getConsumerMessageTypes().contains(MessageType.ALL)) {
                    consumer.consume(message);
                }
            }
        }
    }

    public void deleteMessage() throws Exception {
        synchronized (queue) {
            if (queue.size() == 0){
                throw new Exception("min size");
            }
            queue.remove(0);
        }
    }

    public boolean subscribe(Consumer consumer) {
        synchronized (consumers) {
            consumers.add(consumer);
            orderResolver.resolveConsumerOrder(consumers);
        }
        return true;
    }

    public ArrayList<Consumer> getConsumers() {
        return consumers;
    }

}
