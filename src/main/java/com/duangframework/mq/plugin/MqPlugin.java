package com.duangframework.mq.plugin;

import com.duangframework.mq.core.IMqClient;
import com.duangframework.mq.core.MqConnection;
import com.duangframework.mq.core.MqFactory;
import com.duangframework.mq.emq.EmqClient;
import com.duangframework.mvc.plugin.IPlugin;

/**
 * Created by laotang on 2019/1/6.
 */
public class MqPlugin implements IPlugin {

    private MqConnection conn;
    public MqPlugin(MqConnection c) {
       this.conn = c;
    }

    @Override
    public void start() throws Exception {
        IMqClient mqClient = new EmqClient(conn);
        MqFactory.setClient(mqClient.getClass().getName(), mqClient);
    }

    @Override
    public void stop() throws Exception {
        MqFactory.getDefaultClient().disconnect();
    }
}
