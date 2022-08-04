package com.phonepe.queue.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

    private MessageType messageType;

    private JsonNode jsonNode;

    private final ObjectMapper map = new ObjectMapper();

    private String val;

    public Message(MessageType messageType, String val) throws JsonProcessingException {
        this.messageType = messageType;
        this.val = val;
        this.jsonNode = map.readTree(val);
    }

    public Message(MessageType messageType, JsonNode jsonNode) throws Exception {
        this.messageType = messageType;
        this.jsonNode = jsonNode;
        this.val = jsonNode.toString();
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

}
