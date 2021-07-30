package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import lombok.Data;

@Data
public class Proxy implements IProxy {
    String host;
    Integer port;
    String login;
    String pass;
    ProxyStatus status;

    public Proxy(String host, Integer port, String login, String pass) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.pass = pass;
        this.status = ProxyStatus.UNCHECKED;
    }

    public Proxy(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.status = ProxyStatus.UNCHECKED;
    }
}
