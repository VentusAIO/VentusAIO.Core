package com.ventus.core.task;

import com.ventus.core.exceptions.Not200CodeException;
import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Profile;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Request;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import com.ventus.core.proxy.ProxyManager;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Шаблон для создания Request модулей
 */
abstract public class RequestModule implements Runnable {

    @Getter
    private final HashMap<String, String> cookiesMap = new HashMap<>();
    protected String sessionCookies;
    private Sender sender = new Sender();
    private LinkedList<IProfile> profiles = new LinkedList<>();
    @Setter
    private String itemId;
    protected boolean sendCookie = true;
    @Getter
    StringBuilder cookieStringBuilder;

    /**
     * Базовый метод для отправки запроса
     *
     * @param method            - POST, GET, PATCH
     * @param link              - ссылка, по которой надо производить запрос
     * @param json              - строка вида json, которая передается в запрсое
     * @param requestProperties - body http/s запроса из config
     * @param isDoIn            - открывать ли поток ввода для получения данных с сервера
     * @return ответ с сервера
     * @throws Exception если код не 200
     */
    protected Response send(String method, String link, String json, Map<String, String> requestProperties, InputStreamTypes isDoIn) throws Exception {

        cookieStringBuilder = new StringBuilder();
        if (sessionCookies != null) {
            cookieStringBuilder.append(sessionCookies);
            cookieStringBuilder.append("; ");
        }
        if (!cookiesMap.isEmpty()) {
            for (Map.Entry<String, String> entry : cookiesMap.entrySet()) {
                cookieStringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
            cookieStringBuilder = new StringBuilder(cookieStringBuilder.substring(0, cookieStringBuilder.length() - 2));
        }
        if (cookieStringBuilder.toString().equals("")) {
            cookieStringBuilder = new StringBuilder("null");
        }

        Request request = new Request();

        request.setLink(link);
        request.setMethod(method);
        request.setDoIn(isDoIn);
        request.setData(json);

        //"cookieStringBuilder" вместо "cookie" <?>
        if (sendCookie) {
            request.addRequestProperties("cookie", cookieStringBuilder.toString());
        }

        System.out.println("-------------------------------");
        System.out.println(cookieStringBuilder.toString());
        System.out.println("===============================");

        if (requestProperties != null && !requestProperties.isEmpty()) {
            requestProperties.forEach(request::addRequestProperties);
        }

        Response response = sender.send(request);
        postSend(response);

        if (!response.getSetCookies().isEmpty()) {
            for (Map.Entry<String, String> entry : response.getSetCookies().entrySet()) {
                cookiesMap.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }

        if (response.getResponseCode() != 200 && response.getResponseCode() != 302 && response.getResponseCode() != 404) {
            throw new Not200CodeException(response.getResponseCode());
        }

        return response;
    }

    /**
     * Метод для переопределения в дочерних классах, в котором модифицируется запрос для работы с конкретным модулем.
     *
     * @param response - запрос, который модифицируется
     * @throws Exception -
     */
    protected abstract void postSend(Response response) throws Exception;

    protected void configureProxy(ProxyManager proxyManager) {
        IProxy proxy = proxyManager.getProxy();
        sender = new Sender(proxy);
        sender.setProxyManager(proxyManager);
    }

    ;

    private void sendAfter(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {
        try {
            start();
            sendAfter("OK");
        } catch (Exception e) {
            sendAfter(e.getMessage());
        }
    }

    public abstract void start();

    public synchronized IProfile getProfile() {
        return profiles.pop();
    }

    public void setProfileGroup(List<IProfile> profiles) {
        this.profiles = new LinkedList<>(profiles);
    }
}