package com.duangframework.mq.core;

import java.util.List;
import java.util.Set;

/**
 * Created by laotang on 2019/1/6.
 */
public interface IMqClient {


    /**
     * 发布
     * @param messageDto
     */
    void publish(MqMessage messageDto);

    /**
     * 订阅
     */
    void subscribe(IMessageListener listener);

    /**
     * 取消订阅
     * @param topic
     */
    void unsubscribe(String topic);

    /**
     * 取得所有订阅主题
     */
    Set<String> getAllTopic();


}
