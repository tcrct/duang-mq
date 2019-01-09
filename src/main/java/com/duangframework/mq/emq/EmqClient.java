package com.duangframework.mq.emq;

import com.duangframework.exception.MvcException;
import com.duangframework.kit.ToolsKit;
import com.duangframework.mq.core.IMessageListener;
import com.duangframework.mq.core.IMqClient;
import com.duangframework.mq.core.MqConnection;
import com.duangframework.mq.core.MqMessage;
import com.duangframework.mq.utils.MqUtils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Set;

/**
 * #号特殊字符是不允许出现到MQTT服务器
 */

public class EmqClient implements IMqClient {

    private MqttClient mqttClient;
    private MqttConnectOptions connOpts;
    private MqConnection conn;

    public EmqClient(MqConnection conn) {
        this(conn.getBroker(), conn.getClientId(), conn.getAccount(), conn.getPassword());
        this.conn = conn;
    }

    public EmqClient(String broker, String clientId, String userName, String password) {
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectOptions();
            connOpts.setAutomaticReconnect(true);
            connOpts.setCleanSession(true);
            connOpts.setUserName(userName);
            connOpts.setPassword(password.toCharArray());
            mqttClient.connect(connOpts);
            System.out.println("connecting to broker: "+broker + " is success!");
        } catch (Exception e) {
            throw new MvcException("start up mqtt server fail: " + e.getMessage(), e);
        }
    }


    @Override
    public void publish(MqMessage messageDto) {
        if(ToolsKit.isEmpty(mqttClient)) {
            throw new NullPointerException("mqttClient is null");
        }
        connOpts.setCleanSession(true);
        MqttMessage mqttMessage = new MqttMessage(messageDto.getMessage().getBytes());
        mqttMessage.setQos(messageDto.getQos());
        try {
            mqttClient.publish(messageDto.getTopic(), mqttMessage);
            System.out.println("publish ["+messageDto.getTopic()+"] to mqtt server is success!");
        } catch (Exception e) {
            throw new MvcException("publish ["+messageDto.getTopic()+"] to mqtt server is fail: " + e.getMessage(), e);
        }
    }

    @Override
    public void subscribe(IMessageListener listener) {
        if (ToolsKit.isEmpty(mqttClient)) {
            throw new NullPointerException("mqttClient is null");
        }
        System.out.println(mqttClient.getServerURI());
        connOpts.setCleanSession(true);
        try {
            mqttClient.subscribe(listener.getTopic(), listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getAllTopic() {
        String url = MqUtils.getEqmHttpUrl(conn, mqttClient.getServerURI(), EmqEnums.GET_ALL_TOPIC.getTarget());
        if(ToolsKit.isEmpty(url)) {
            throw new NullPointerException("获取集群主题信息时，API地址为空");
        }
        return MqUtils.getAllTopic(url, MqUtils.createBaseAuthHeaderString(connOpts.getUserName(), new String(connOpts.getPassword())));
    }


}
