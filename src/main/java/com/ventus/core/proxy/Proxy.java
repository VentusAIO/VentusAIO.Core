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
}
