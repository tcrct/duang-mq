package com.duangframework.mq;

import com.duangframework.kit.HttpKit;
import com.duangframework.mq.core.*;
import com.duangframework.mq.plugin.MqPlugin;
import com.duangframework.mvc.http.HttpResponse;
import com.duangframework.mvc.http.enums.ContentTypeEnums;
import com.duangframework.net.http.HttpResult;

import java.util.Set;

public class Demo {

    public static void main(String[] args) {

//            try {
//                while (true) {
//                    Thread.sleep(1000);
//                    Demo.test();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        Demo.main2(null);
    }

    public static void test() {
        String body = "{\n" +
                "\t\"tokenid\":\"123\",\n" +
                "\t\"id\":\"5c3025e596accd3844e0638a\",\n" +
                "\t\"name\":\"laotang\",\n" +
                "\t\"sex\":1,\n" +
                "\t\"address\":\"11111\"\n" +
                "\t\n" +
                "}";
//        body = body.replace("\t","").replace("\n","");
//        System.out.println(body);
        HttpResult result = HttpKit.duang()
                .header("content-type", ContentTypeEnums.JSON.getValue())
                .url("http://192.168.100.99:9090/openapi/demo/getbean")
                .body(body)
                .post();
        if(result.isSuccess()) {
            System.out.println(result.getResult());
        }

    }

    public static void main2(String[] args) {
        String topic = "signet/3/4";
        String message = "这是一封迟来的告白！！test!@456思格特distributionManagement";
        String account = "admin";
        String password = "1b88ab6d";
        // 必须要唯一，如果有重复ID的话，会导致抛出客户机没连接异常
        // https://www.ibm.com/support/knowledgecenter/zh/SSFKSJ_7.5.0/com.ibm.mm.tc.doc/tc80300_.htm
        String clientId = "signet-mqtt-java-laotang";
        String broker = "tcp://192.168.100.55:1883";
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
//        subscribe("signet/1/2/777");
        subscribe(topic);
        // 发布
//        try {
//            int i=0;
//            do {
//                Thread.sleep(2000);
//                publish(topic, message);
//                i++;
//            } while (i <= 100);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void publish(String topic, String message) {
        MqFactory.getDefaultClient().publish(new MqMessage(topic, message));
    }

    private static void subscribe(String... topics){
        for(String topic : topics) {
            IMqCallback<MqResult> callback = new IMqCallback<MqResult>(){
                @Override
                public void callback(MqResult result) {
                    if(result.isComplete()) {
                        try {
                            System.out.println("subscribe:  " + result.getTopic() + "  ########  " + result.getMessageId()+ " ########: " + result.getBodyString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            MqFactory.getDefaultClient().subscribe(topic, callback);

        }
    }

    private static  Set<String> getAllTopic(){
        return MqFactory.getDefaultClient().getAllTopic();
    }
}
