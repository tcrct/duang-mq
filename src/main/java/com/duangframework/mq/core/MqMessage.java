package com.duangframework.mq.core;

import com.duangframework.utils.DuangId;

/**
 * 要发布的信息对象
 */
public class MqMessage implements java.io.Serializable {

    public String topic;
    public String messageId;
    public String message;
    public int qos = 1;

    public MqMessage() {}

    public MqMessage(String topic, String message) {
        this(topic,new DuangId().toString(), message, 1);
    }

    public MqMessage(String topic, String messageId, String message, int qos) {
        this.topic = topic;
        this.messageId = messageId;
        this.message = message;
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }
}
