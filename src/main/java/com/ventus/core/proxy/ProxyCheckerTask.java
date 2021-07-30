package com.ventus.core.proxy;

import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Request;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import lombok.SneakyThrows;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.ProxySelector;
import java.net.http.HttpClient;

public class ProxyCheckerTask implements Runnable{
    private final String host;
    private final ProxyPair proxyPair;
    private final Sender sender;

    @SneakyThrows
    @Override
    public void run() {
        String result;

        Request request = new Request();
//        request.setRequestProperties(AdidasConfig.getAdidas());
        request.setLink(host);
        request.setMethod("GET");
        request.setDoIn(InputStreamTypes.NONE);

        Response response = sender.send(request);

        proxyPair.status = (response.getResponseCode() == 200 || response.getResponseCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID;
        result = String.format("Proxy: {%s} -- %s[%d], for host: %s", proxyPair.proxy, proxyPair.status, response.getResponseCode(), host);
        ProxyChecker.callback(result, proxyPair);
    }

    public ProxyCheckerTask(ProxyPair proxyPair, String host) {
        this.proxyPair = proxyPair;
        this.host = host;
        this.sender = new Sender();
        sender.setHttpClient(
                HttpClient.newBuilder()
                        .proxy(ProxySelector.of((InetSocketAddress) proxyPair.proxy.address()))
                        .authenticator(new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        proxyPair.credentials.getUsername(),
                                        proxyPair.credentials.getPassword().toCharArray()
                                );
                            }
                        })
                        .build()
        );
    }
}
