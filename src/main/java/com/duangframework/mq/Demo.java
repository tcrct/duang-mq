package com.duangframework.mq;

import com.duangframework.mq.core.*;
import com.duangframework.mq.plugin.MqPlugin;

import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        String topic = "test!@456思格特";
        String message = "这是一封迟来的告白！！test!@456思格特distributionManagement";
        String account = "admin";
        String password = "public";
        // 必须要唯一，如果有重复ID的话，会导致抛出客户机没连接异常
        // https://www.ibm.com/support/knowledgecenter/zh/SSFKSJ_7.5.0/com.ibm.mm.tc.doc/tc80300_.htm
        String clientId = "signet-mqtt-java-laotang";
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
//            Set<String> topicSet = getAllTopic();
//            // 暂停2秒
//            Thread.sleep(2000);
//            if(ToolsKit.isNotEmpty(topicSet)) {
//                // 订阅
//                subscribe(topicSet.toArray(new String[]{}));
//                subscribe(topicSet.toArray(new String[]{}));
//            }
//            Thread.sleep(10000); //10秒后，重新订阅
//            System.out.println("####################");
                subscribe(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void publish(String topic, String message) {
        MqFactory.getDefaultClient().publish(new MqMessage(topic, message));
    }
    private static void subscribe(String... topics){
        for(String topic : topics) {
            MqResult result = new MqResult();
            result = MqFactory.getDefaultClient().subscribe(topic, result);
            if(result.isComplete()) {
                try {
                    System.out.println(result.getTopic() + "  ########  " + result.getMessageId()+ " ########: " + result.getBodyString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static  Set<String> getAllTopic(){
        return MqFactory.getDefaultClient().getAllTopic();
    }
}
