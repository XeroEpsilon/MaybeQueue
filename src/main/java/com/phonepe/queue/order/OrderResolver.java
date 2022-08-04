package com.phonepe.queue.order;

import com.phonepe.queue.Consumer;

import java.util.List;

public interface OrderResolver {
    //update the order in place
    public void resolveConsumerOrder(List<Consumer> consumers);
}
