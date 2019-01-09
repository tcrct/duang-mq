package com.duangframework.mq.core;

public abstract class AbstractMqListener implements IMessageListener{

    protected  String topic;

    public AbstractMqListener(String topic) {
        this.topic = topic;
    }


    @Override
    public String getTopic() {
        return topic;
    }
}
