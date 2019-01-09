package com.duangframework.mq.emq;

/**
 * MQTT HTTP接口
 * @see  http://192.168.100.100:18083/#/http_api
 */
public enum EmqEnums {

    HTTP_PORT("18083", "控制台HTTP默认端口"),
    GET_ALL_TOPIC("/api/v2/routes","获取集群主题信息"),

    ;

    private String target;
    private String desc;

    private EmqEnums(String target, String desc) {
        this.target = target;
        this.desc = desc;
    }

    public String getTarget() {
        return target;
    }

    public String getDesc() {
        return desc;
    }
}
