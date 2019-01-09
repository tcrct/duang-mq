package com.duangframework.mq.core;

import com.duangframework.kit.ToolsKit;

import java.util.Map;

/**
 * Created by laotang on 2019/1/6.
 */
public class MqFactory {

    private static Map<String, IMqClient> INSTANCE_MAP = new java.util.concurrent.ConcurrentHashMap<>();
    private static String defaultMqClientKey;


    public static IMqClient getDefaultClient() {
        return getClient(defaultMqClientKey);
    }

    public static IMqClient getClient(String key) {
        return INSTANCE_MAP.get(key);
    }

    public static void setClient(String key, IMqClient client) {
        if(ToolsKit.isEmpty(defaultMqClientKey)) {
            defaultMqClientKey = key;
        }
        INSTANCE_MAP.put(key, client);
    }

}
