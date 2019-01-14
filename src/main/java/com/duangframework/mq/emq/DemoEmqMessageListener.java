package com.duangframework.mq.emq;

import com.duangframework.kit.HttpKit;
import com.duangframework.mq.core.AbstractMqListener;
import com.duangframework.mq.core.MqFactory;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DemoEmqMessageListener extends AbstractMqListener {

    private String callback;

    public DemoEmqMessageListener(String topic, String callback) {
        super(topic);
        this.callback = callback;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(topic + "     ##########:" + new String(message.getPayload(), "UTF-8"));
        // 返回给第三方的回调信息
//        String payLoad = new String(message.getPayload());

//        HttpKit.duang().url(callback).body(payLoad).post();

        MqFactory.getDefaultClient().unsubscribe(topic);

    }
}
