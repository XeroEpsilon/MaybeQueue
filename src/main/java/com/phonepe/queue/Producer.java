package com.phonepe.queue;

import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.Queue;

import java.util.List;

public class Producer {

    private final List<Queue> queues;

    public Producer(List<Queue> queues) {
        this.queues = queues;
    }

    public boolean produceMessage(Message message) throws Exception{
        for (Queue q: queues) {
            q.pushMessage(message);
        }
        return true;
    }

    public List<Queue> getQueues() {
        return queues;
    }

}
