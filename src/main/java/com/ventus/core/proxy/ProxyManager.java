package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.network.Sender;
import lombok.extern.slf4j.Slf4j;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ProxyManager {
    private final List<IProxy> availableProxies = new CopyOnWriteArrayList<>();
    private volatile int counter = 0;

    public ProxyManager() {
    }

    public void addProxyList(List<IProxy> proxies) {
        availableProxies.addAll(proxies);
    }

    // TODO: return Optional of IProxy
    public synchronized IProxy getProxy() {
        int size = availableProxies.size();
        int localCounter = 0;
        IProxy proxyPair;
        // TODO: add cycle exit if all proxies INVALID (Done, but ugly)
        do {
            int proxyId = (int) (counter++ % availableProxies.size());
            proxyPair = availableProxies.get(proxyId);
        } while (proxyPair.getStatus() != ProxyStatus.VALID || proxyPair.getStatus() != ProxyStatus.UNCHECKED || (size - localCounter++) > 0);
        return proxyPair;
    }

    public synchronized void replaceProxy(Sender sender) {
        if (sender.getProxy() == null) return;
        IProxy proxy = sender.getProxy();
        Optional<IProxy> proxyPair = Optional.empty();
        for (IProxy pp : availableProxies) {
            if (pp.equals(proxy)) {
                proxyPair = Optional.of(pp);
                break;
            }
        }
        proxyPair.ifPresent(pair -> pair.setStatus(ProxyStatus.INVALID));

        IProxy proxy_new = getProxy();
        log.info("Switching to another proxy: " + proxy.getHost() + ":" + proxy.getPort() + " --> " + proxy_new.getHost() + ":" + proxy_new.getPort());
        sender.setProxy(proxy_new);
        sender.setHttpClient(HttpClient.newBuilder()
                .cookieHandler(sender.getCookieManager())
                .proxy(ProxySelector.of(new InetSocketAddress(proxy_new.getHost(), proxy_new.getPort())))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxy_new.getLogin(), proxy_new.getPass().toCharArray());
                    }
                })
                .build());
        counter++;
    }

    public void disableProxy(Sender sender) {
        sender.setHttpClient(HttpClient.newBuilder().cookieHandler(sender.getCookieManager()).build());
    }

    public void enableProxy(Sender sender) {
        IProxy proxy = sender.getProxy();
        sender.setHttpClient(HttpClient.newBuilder()
                .cookieHandler(sender.getCookieManager())
                .proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                proxy.getLogin(),
                                proxy.getPass().toCharArray()
                        );
                    }
                })
                .build());
    }
}
