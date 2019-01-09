package com.duangframework.mq;

import com.duangframework.kit.ToolsKit;
import com.duangframework.mq.core.*;
import com.duangframework.mq.emq.EmqMessageListener;
import com.duangframework.mq.plugin.MqPlugin;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import sun.plugin2.jvm.RemoteJVMLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        String topic = "test!@456思格特";
        String message = "这是一封迟来的告白！！test!@456思格特distributionManagement";
        String account = "admin";
        String password = "public";
        String clientId = "signet-mqtt-java";
        String broker = "tcp://192.168.100.100:1883";
        MqPlugin plugin = new MqPlugin(new MqConnection.Builder()
                .account(account)
                .password(password)
                .clientId(clientId)
                .broker(broker)
//                .httpPort(18083)
                .isDefault(true)
                .build());
        try {
            plugin.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 发布
        publish(topic, message);
        try {
            // 暂停2秒
            Thread.sleep(2000);

            Set<String> topicSet = getAllTopic();
            if(ToolsKit.isNotEmpty(topicSet)) {
                // 订阅
                subscribe(topicSet.toArray(new String[]{}));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void publish(String topic, String message) {
        MqFactory.getDefaultClient().publish(new MqMessage(topic, message));
    }
    private static void subscribe(String... topics){
        for(String topic : topics) {
            MqFactory.getDefaultClient().subscribe(new EmqMessageListener(topic));
        }
    }

    private static  Set<String> getAllTopic(){
        return MqFactory.getDefaultClient().getAllTopic();
    }
}
