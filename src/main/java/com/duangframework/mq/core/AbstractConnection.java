package com.duangframework.mq.core;

/**
 * Created by laotang on 2019/1/6.
 */
public class AbstractConnection<T extends IMqConnection> {

    public static <T> T getConnection(IMqConnection connection) {
        return (T)connection.getConnection();
    }

}
