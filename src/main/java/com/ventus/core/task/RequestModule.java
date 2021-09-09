package com.ventus.core.task;

import com.ventus.core.exceptions.Not200CodeException;
import com.ventus.core.interfaces.IAccount;
import com.ventus.core.interfaces.IProfile;
import com.ventus.core.models.Account;
import com.ventus.core.models.AccountManager;
import com.ventus.core.models.ProfileManager;
import com.ventus.core.network.*;
import com.ventus.core.proxy.ProxyManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Callable;


/**
 * Шаблон для создания Request модулей
 */
@Slf4j
abstract public class RequestModule implements Callable<Map<?, ?>> {
    @Getter
    protected final HashMap<String, String> cookiesMap = new HashMap<>();

    @Setter
    protected String itemId;

    @Setter
    protected AvailabilityFilters filter;

    @Setter
    protected String[] sizes;

    @Getter
    StringBuilder cookieStringBuilder;

    StringBuilder user_logs = new StringBuilder();
    StringBuilder admin_logs = new StringBuilder();

    private ProfileManager profileManager = new ProfileManager();
    private AccountManager accountManager = new AccountManager();

    protected Sender sender = new Sender();


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

        Request request = new Request();

        request.setLink(link);
        request.setMethod(method);
        request.setDoIn(isDoIn);
        request.setData(json);

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

    public void configureProxy(ProxyManager proxyManager) {
        sender.changeProxy(proxyManager.getProxy());
        sender.setProxyManager(proxyManager);
    }

    @Override
    public abstract Map<?, ?> call();

    public synchronized IProfile getProfile() {
        IProfile profile = profileManager.getProfile();
        if (profile == null) throw new NoSuchElementException("list is empty");
        return profile;
    }

    public synchronized IAccount getAccount() {
        IAccount account = accountManager.getAccount();
        if (account == null) throw new NoSuchElementException("list is empty");
        return account;
    }

    public void setProfileManger(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void setAccountManger(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /* Logs */
    void ulog(String message) {
        user_logs.append(message).append("\n");
    };
    void log(String message) {
        admin_logs.append(message).append("\n");
    };
    String getUserLogs() {
        return user_logs.toString();
    }
    String getAdminLogs() {
        return user_logs.toString();
    }
}
