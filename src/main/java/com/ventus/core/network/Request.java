package com.ventus.core.network;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс в котором храниться информации о запросе, который будет отправляться на сервер.
 */
@Data
public class Request {
    /**
     * Поля, которые надо отправить в хедере запроса
     */
    private Map<String, String> requestProperties = new HashMap<>();
    /**
     * URL на который надо отправить запрос
     */
    private String link;
    /**
     * Метод POST, GET, PATCH
     */
    private String method;
    /**
     * Информация, которую надо передать. Обычно JSON строка
     */
    private String data;
    /**
     * Enum отвечающий за прием информации с сервера. Нужно ли получить информацию с сервера(HTML, JSON, BR, GZIP)
     */
    private InputStreamTypes doIn;


    public void addRequestProperties(String key, String value) {
        requestProperties.put(key, value);
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestProperties=" + requestProperties +
                ", link='" + link + '\'' +
                ", method='" + method + '\'' +
                ", data='" + data + '\'' +
                ", doIn=" + doIn +
                '}';
    }

}
