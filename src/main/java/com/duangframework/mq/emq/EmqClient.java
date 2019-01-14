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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * #号特殊字符是不允许出现到MQTT服务器
 */

public class EmqClient implements IMqClient {

    private static final Logger logger = LoggerFactory.getLogger(EmqClient.class);

    private MqttClient mqttClient;
    private MqttConnectOptions connOpts;
    private MqConnection conn;
    private Set<String> subscribeTopicSet;
    private Set<String> publishTopicSet;

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
            subscribeTopicSet = new HashSet<>();
            publishTopicSet = new HashSet<>();
            logger.warn("connecting to broker: "+broker + " is success!");
        } catch (Exception e) {
            throw new MvcException("start up mqtt server fail: " + e.getMessage(), e);
        }
    }


    @Override
    public void publish(MqMessage messageDto) {
        if(ToolsKit.isEmpty(mqttClient)) {
            throw new NullPointerException("mqttClient is null");
        }
        String topic = messageDto.getTopic();
        if(publishTopicSet.contains(topic)){
            logger.warn("topic: " + topic + " is exist, exit publish");
            return;
        }
        connOpts.setCleanSession(true);
        MqttMessage mqttMessage = new MqttMessage(messageDto.getMessage().getBytes());
        mqttMessage.setQos(messageDto.getQos());
        try {
            mqttClient.publish(topic, mqttMessage);
            publishTopicSet.add(topic);
            logger.warn("sucessfully published message: "+new String(mqttMessage.getPayload())+" to topic: "+ topic+" (QoS "+ messageDto.getQos()+")");
        } catch (Exception e) {
            publishTopicSet.remove(topic);
            throw new MvcException("publish ["+messageDto.getTopic()+"] to mqtt server is fail: " + e.getMessage(), e);
        }
    }

    @Override
    public void subscribe(IMessageListener listener) {
        if (ToolsKit.isEmpty(mqttClient)) {
            throw new NullPointerException("mqttClient is null");
        }
        String topic = listener.getTopic();
        if(subscribeTopicSet.contains(topic)){
            logger.warn("topic: " + topic + " is exist, exit subscribe");
            return;
        }
        connOpts.setCleanSession(true);
        try {
            mqttClient.subscribe(topic, listener);
            subscribeTopicSet.add(topic);
            logger.warn("sucessfully subscribe topic: " + topic);
        } catch (Exception e) {
            subscribeTopicSet.remove(topic);
            throw new MvcException("subscribe ["+topic+"] to mqtt server is fail: " + e.getMessage(), e);
        }
    }

    @Override
    public void unsubscribe(String topic) {
        try {
            mqttClient.unsubscribe(topic);
            logger.warn("sucessfully unsubscribe topic: " + topic);
        } catch (Exception e) {
            throw new MvcException("unsubscribe ["+topic+"] to mqtt server is fail: " + e.getMessage(), e);
        } finally {
            subscribeTopicSet.remove(topic);
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
