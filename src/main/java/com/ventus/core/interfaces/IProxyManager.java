package com.ventus.core.interfaces;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.network.Sender;

import java.util.List;

public interface IProxyManager {
    void addProxyList(List<IProxy> proxies);

    IProxy getProxy();

    void replaceProxy(Sender sender);

    void disableProxy(Sender sender);

    void enableProxy(Sender sender);
}
