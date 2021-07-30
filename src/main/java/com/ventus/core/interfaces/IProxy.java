package com.ventus.core.interfaces;

import com.ventus.core.proxy.ProxyStatus;


public interface IProxy {

    String getLogin();

    String getPass();

    String getHost();

    Integer getPort();

    ProxyStatus getStatus();

    void setStatus(ProxyStatus proxyStatus);
}
