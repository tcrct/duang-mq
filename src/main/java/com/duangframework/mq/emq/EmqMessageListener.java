package com.duangframework.mq.emq;

import com.duangframework.mq.core.AbstractMqListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class EmqMessageListener extends AbstractMqListener {

    public EmqMessageListener(String topic) {
        super(topic);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("##########:" + new String(message.getPayload(), "UTF-8"));
    }
}
