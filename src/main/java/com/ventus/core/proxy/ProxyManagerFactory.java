package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;

import java.util.ArrayList;
import java.util.List;

public class ProxyManagerFactory {
    public static ProxyManagerImpl createProxyManager(String host) {
        ProxyManagerImpl proxyManager = new ProxyManagerImpl();
        proxyManager.addProxyList(ProxyChecker.check(getProxyList(),host));
        return proxyManager;
    }

    private static List<IProxy> getProxyList() {
        return new ArrayList<>();
    }
}
