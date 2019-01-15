package com.duangframework.mq.core;

public interface IMqCallback<T> {
    void callback(T t);
}
