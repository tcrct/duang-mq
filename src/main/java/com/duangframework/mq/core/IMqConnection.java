package com.duangframework.mq.core;

/**
 * Created by laotang on 2019/1/6.
 */
public interface IMqConnection {

    <T> T getConnection();

}
