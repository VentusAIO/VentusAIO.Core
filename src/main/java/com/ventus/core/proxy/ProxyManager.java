package com.ventus.core.proxy;

import com.ventus.core.network.Sender;
import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.net.http.HttpClient;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
public class ProxyManager {

    private final LinkedHashMap<Proxy, Credentials> proxyCredentialsMap = new LinkedHashMap<>();
    private final List<ProxyPair> availableProxies = new CopyOnWriteArrayList<>();

    private volatile int counter = 0;

    public ProxyManager() {

    }

    public void addProxy(Proxy proxy, Credentials credentials) {
        ProxyPair proxyPair = new ProxyPair(proxy, credentials, ProxyStatus.VALID);
        availableProxies.add(proxyPair);
        proxyCredentialsMap.putIfAbsent(proxy, credentials);
    }

    // tmp method, refactor later
    public void addProxyList(List<ProxyPair> proxies) {
        proxyCredentialsMap.putAll(proxies.stream()
                .collect(Collectors.toMap(
                        ProxyPair::getProxy,
                        ProxyPair::getCredentials
                ))
        );
        availableProxies.addAll(proxies);
    }

    public void addProxy(ProxyPair proxyPair) {
        if (proxyPair == null) {
            return;
        }
        availableProxies.add(proxyPair);
        proxyCredentialsMap.putIfAbsent(proxyPair.proxy, proxyPair.credentials);
    }

    public synchronized Proxy getProxy() {
        ProxyPair proxyPair;
        // TODO: add cycle exit if all proxies INVALID
        do {
            int proxyId = (int) (counter++ % availableProxies.size());
            proxyPair = availableProxies.get(proxyId);
        } while (proxyPair.status != ProxyStatus.VALID);
        return proxyPair.proxy;
    }

    public void replaceProxy(Sender sender) {
        if (sender.getWebProxy() == null) return;
        Proxy proxy = sender.getWebProxy();
        Optional<ProxyPair> proxyPair = availableProxies.stream().filter(pp -> pp.proxy == proxy).findFirst();
        proxyPair.ifPresent(pair -> pair.status = ProxyStatus.INVALID);

        Proxy proxy_new = getProxy();
        log.info("Switching to another proxy: " + proxy.address() + " --> " + proxy_new.address());
        sender.setWebProxy(proxy_new);
        sender.setHttpClient(HttpClient.newBuilder()
                .proxy(ProxySelector.of((InetSocketAddress) proxy_new.address()))
                .authenticator(getAuthenticator(proxy_new))
                .build());
        counter++;
    }

    public Authenticator getAuthenticator (Proxy proxy) {
        Credentials credentials = proxyCredentialsMap.get(proxy);
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(credentials.getUsername(), credentials.getPassword().toCharArray());
            }
        };
    }

    public void disableProxy (Sender sender) {
        sender.setHttpClient(HttpClient.newHttpClient());
    }

    public void enableProxy (Sender sender) {
        Proxy proxy = sender.getWebProxy();
        sender.setHttpClient(HttpClient.newBuilder()
                .proxy(ProxySelector.of((InetSocketAddress) proxy.address()))
                .authenticator(getAuthenticator(proxy))
                .build());
    }
}
