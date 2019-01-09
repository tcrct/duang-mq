package com.duangframework.mq.core;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

public interface IMessageListener extends IMqttMessageListener {

    String getTopic();

}
