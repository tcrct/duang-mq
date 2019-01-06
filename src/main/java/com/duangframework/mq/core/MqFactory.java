package com.duangframework.mq.core;

import com.duangframework.mq.emq.EmqConnection;

/**
 * Created by laotang on 2019/1/6.
 */
public class MqFactory {

    private IMqClient mqClient;

    public IMqClient getClient() {
        return mqClient;
    }

    public void setClient(IMqClient client) {
        AbstractConnection.getConnection(new EmqConnection());
        this.mqClient = client;
    }




}
