package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
public class ProxyChecker {
    static {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
    }

    public static ProxyStatus check(IProxy iProxy, String url) {
        HttpClient httpClient = HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(iProxy.getHost(), iProxy.getPort())))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(iProxy.getLogin(), iProxy.getPass().toCharArray());
                    }
                })
                .build();

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .timeout(Duration.ofMillis(10_000))
                .uri(URI.create(url))
                .build();

        ProxyStatus result;
        int statusCode = -1;
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            result = (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID;
            statusCode = httpResponse.statusCode();
            log.info(String.format("Proxy: {%s} -- %s [%d], for host: %s", iProxy.getHost(), result, statusCode, url));
        } catch (IOException e) {
            result = ProxyStatus.INVALID;
            log.info(String.format("Proxy: {%s} -- %s [%d], for host: %s", iProxy.getHost(), result, statusCode, url));
        } catch (InterruptedException e) {
            result = ProxyStatus.INVALID;
            log.info("Thread was interrupted caused by: " + e.getMessage());
        }
        return result;
    }
//    public static List<IProxy> check(List<IProxy> proxyPairs, String host) {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        for (IProxy proxyPair : proxyPairs) {
//            executorService.submit(new ProxyCheckerTask(proxyPair, host));
//        }
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return proxyPairs;
//    }
}
