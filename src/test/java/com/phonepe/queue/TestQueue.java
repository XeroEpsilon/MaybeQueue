package com.phonepe.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.phonepe.queue.order.DependencyResolver;
import com.phonepe.queue.order.OrderResolver;
import com.phonepe.queue.queue.Message;
import com.phonepe.queue.queue.MessageType;
import com.phonepe.queue.queue.Queue;
import com.phonepe.queue.utils.AssertConsumer;
import com.phonepe.queue.utils.AssertConsumer2;
import com.phonepe.queue.utils.FailConsumer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueue {

    @Test
    public void testQueueSubscribe() {
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(10, dependencyResolver);
        Consumer consumer = new SimpleConsumer(List.of(MessageType.ALL), List.of());
        assertTrue(consumer.subscribe(List.of(queue)));
        assertTrue(queue.getConsumers().contains(consumer));
    }

    @Test
    public void testProducerCreation() {
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(10, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        assertTrue(producer.getQueues().contains(queue));
    }

    @Test
    public void testQueueMaxSize() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("no exception"));
        }
        try {
            Message message = new Message(MessageType.T1, "{}");
            assertFalse(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("max size"));
        }
    }

    @Test
    public void testQueueConsumption() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        Consumer consumer = new AssertConsumer(List.of(MessageType.ALL), List.of());
        consumer.subscribe(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("no exception"));
        }

    }

    @Test
    public void testQueueConsumptionWithFailConsumer() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        Consumer consumer = new FailConsumer(List.of(MessageType.ALL), List.of());
        consumer.subscribe(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            producer.produceMessage(message);
        } catch (AssertionError e) {
            //Success to skip
        } catch (Exception e) {
            assertEquals(1, 2);
        }

    }

    @Test
    public void testQueueMultiConsumption() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        Consumer consumer1 = new AssertConsumer(List.of(MessageType.ALL), List.of());
        Consumer consumer2 = new AssertConsumer2(List.of(MessageType.ALL), List.of());
        consumer1.subscribe(List.of(queue));
        consumer2.subscribe(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("no exception"));
        }
    }

    @Test
    public void testQueueMultiTypeConsumption() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        Consumer consumer1 = new AssertConsumer(List.of(MessageType.ALL), List.of());
        Consumer consumer2 = new AssertConsumer2(List.of(MessageType.T2), List.of());
        consumer1.subscribe(List.of(queue));
        consumer2.subscribe(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("no exception"));
        }
    }

    @Test
    public void testQueueMultiTypeFailConsumption() {
        int size = 1;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer = new Producer(List.of(queue));
        Consumer consumer1 = new AssertConsumer(List.of(MessageType.ALL), List.of());
        Consumer consumer2 = new FailConsumer(List.of(MessageType.T2), List.of());
        consumer1.subscribe(List.of(queue));
        consumer2.subscribe(List.of(queue));
        try{
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("no exception"));
        }
    }

    @Test
    public void testMultiProducer() {
        int size = 3;
        OrderResolver dependencyResolver = new DependencyResolver();
        Queue queue = new Queue(size, dependencyResolver);
        Producer producer1 = new Producer(List.of(queue));
        Producer producer2 = new Producer(List.of(queue));
        try {
            Message message = new Message(MessageType.T1, "{}");
            assertTrue(producer1.produceMessage(message));
            assertTrue(producer2.produceMessage(message));
            assertTrue(producer1.produceMessage(message));
            assertFalse(producer1.produceMessage(message));
        } catch (JsonProcessingException jpe) {
            assertTrue(jpe.getMessage().contains("sanjeev"));
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("max size"));
        }
    }

}
