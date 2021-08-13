package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.Account;
import com.ventus.core.models.TaskGroup;
import com.ventus.core.network.InputStreamTypes;
import com.ventus.core.network.Request;
import com.ventus.core.network.Response;
import com.ventus.core.network.Sender;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

public class ProxyCheckerTask implements Runnable {
    private final IProxy proxy;
    private final String url;
    private final HttpClient httpClient;

    public ProxyCheckerTask(IProxy proxy, String url) {
        this.url = url;
        this.proxy = proxy;

        this.httpClient = HttpClient.newBuilder()
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
                .build();
    }

    @Override
    public void run() {
        String result;

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .timeout(Duration.ofMillis(10_000))
                .uri(URI.create(url))
                .build();

        HttpResponse<String> httpResponse;
        int statusCode = -1;

        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            proxy.setStatus(((httpResponse.statusCode() == 200 || httpResponse.statusCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID));
            statusCode = httpResponse.statusCode();
            result = String.format("Proxy: {%s} -- %s [%d], for host: %s", proxy.getHost(), proxy.getStatus(), statusCode, url);
        } catch (IOException e) {
            proxy.setStatus(ProxyStatus.INVALID);
            result = String.format("Proxy: {%s} -- %s [%d], for host: %s", proxy.getHost(), proxy.getStatus(), statusCode, url);
        } catch (InterruptedException e) {
            proxy.setStatus(ProxyStatus.INVALID);
            result = "Thread was interrupted caused by: " + e.getMessage();
        }

//        proxy.setStatus(((httpResponse.statusCode() == 200 || httpResponse.statusCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID));
//        result = String.format("Proxy: {%s} -- %s [%d], for host: %s", proxy.getHost(), proxy.getStatus(), httpResponse.statusCode(), url);
        ProxyChecker.callback(result, proxy);
    }
}
