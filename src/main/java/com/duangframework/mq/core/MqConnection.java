package com.duangframework.mq.core;

import com.duangframework.mq.emq.EmqEnums;

/**
 * Created by laotang on 2019/1/6.
 */
public class MqConnection implements java.io.Serializable {

    private String broker;
    private String clientId;
    private String account;
    private String password;
    private boolean isDefault;
    private Integer httpPort;

    private MqConnection(String broker, String clientId, String account, String password, boolean isDefault, Integer httpPort) {
        this.broker = broker;
        this.clientId = clientId;
        this.account = account;
        this.password = password;
        this.isDefault = isDefault;
        this.httpPort = httpPort;
    }

    public String getBroker() {
        return broker;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Integer getHttpPort() {
         return  (null == httpPort) ? Integer.valueOf(EmqEnums.HTTP_PORT.getTarget()) : httpPort;
    }

    public static class Builder {
        private String broker;
        private String clientId;
        private String account;
        private String password;
        private boolean isDefault;
        private Integer httpPort;

        public Builder broker(String broker) {
            this.broker = broker;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder httpPort(Integer port) {
            this.httpPort = port;
            return this;
        }

        public Builder isDefault(boolean isDefault) {
            this.isDefault = isDefault;
            return this;
        }

        public MqConnection build() {
            return new MqConnection(broker, clientId, account, password, isDefault, httpPort);
        }
    }

}
