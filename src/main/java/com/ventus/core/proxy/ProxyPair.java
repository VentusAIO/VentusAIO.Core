package com.ventus.core.proxy;

import lombok.Data;

import java.net.Proxy;

@Data
public class ProxyPair {

    Proxy proxy;
    Credentials credentials;
    ProxyStatus status;

    public ProxyPair(Proxy proxy, Credentials credentials, ProxyStatus status) {
        this.proxy = proxy;
        this.credentials = credentials;
        this.status = status;
    }

    public ProxyPair(Proxy proxy, Credentials credentials) {
        this.proxy = proxy;
        this.credentials = credentials;
        this.status = ProxyStatus.IN_PROGRESS;
    }

    public ProxyPair(Proxy proxy) {
        this.proxy = proxy;
        this.credentials = null;
        this.status = ProxyStatus.IN_PROGRESS;
    }
}
