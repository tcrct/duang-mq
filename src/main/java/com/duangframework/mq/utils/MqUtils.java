package com.duangframework.mq.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duangframework.kit.HttpKit;
import com.duangframework.kit.ToolsKit;
import com.duangframework.mq.core.MqConnection;
import com.duangframework.net.http.HttpRequest;
import com.duangframework.net.http.HttpResult;
import com.duangframework.utils.Encodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MqUtils {

    private static final Logger logger = LoggerFactory.getLogger(MqUtils.class);

    public static String getEqmHttpUrl(MqConnection conn, String url, String uri) {
        if(ToolsKit.isNotEmpty(url) && ToolsKit.isNotEmpty(uri)) {
            try {
                URI mqHttpUrl = new URI(url);
                url = url.replace(mqHttpUrl.getScheme(), "http").replace(mqHttpUrl.getPort()+"", conn.getHttpPort() + "");
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            return uri.startsWith("/") ? url + uri : url + "/" + uri;
        }
        return "";
    }

    public static String createBaseAuthHeaderString(String account, String password) {
        String auth = account+":"+password;
        return "Basic " + Encodes.encodeBase64(auth.getBytes()); //Charset.forName("US-ASCII")
    }

    /**
     * 取所有主题
     * @param url
     * @return
     */
    public static Set<String> getAllTopic(String url, String authorization) {
        Set<String> tipicSet = null;
        HttpResult result = HttpKit.duang().header("Authorization", authorization).url(url).get();
        if(result.isSuccess()) {
            String json = result.getResult();
            if (ToolsKit.isNotEmpty(json) && json.length() > 5) {
                JSONObject jsonObject = JSONObject.parseObject(json);
                int code = jsonObject.getInteger("code");
                if (0 == code) {
                    JSONObject resultObj = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = resultObj.getJSONArray("objects");
                    if (ToolsKit.isNotEmpty(jsonArray)) {
                        tipicSet = new HashSet<>(jsonArray.size());
                        for (Iterator<Object> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
                            JSONObject jObj = (JSONObject) iterator.next();
                            tipicSet.add(jObj.getString("topic"));
                        }
                    }
                }
            }
        }
            return tipicSet;
    }
}
