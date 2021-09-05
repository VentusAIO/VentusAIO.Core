package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.util.Pair;
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

    /**
     * Deprecated method, use {@link #checkProxy(IProxy, String)} instead.
     * @param iProxy proxy to check
     * @param url destination address
     * @return ProxyStatus
     */
    @Deprecated()
    public static ProxyStatus check(IProxy iProxy, String url) {
        HttpClient httpClient = getHttpClient(iProxy);

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


    /**
     * Deprecated method, use {@link #checkProxy(IProxy, String)} instead.
     * @param iProxy proxy to check
     * @param url destination address
     * @return String with time spent to do one request to destination address, formatted {@code "{%d} (ms)"}.
     */
    @Deprecated()
    public static String checkRequestTime(IProxy iProxy, String url) {
        HttpClient httpClient = getHttpClient(iProxy);

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .timeout(Duration.ofMillis(10_000))
                .uri(URI.create(url))
                .build();

        String result = "-1 (ms)";
        try {
            long start = System.currentTimeMillis();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            result = String.format("%d (ms)", System.currentTimeMillis() - start);
        } catch (IOException | InterruptedException e) {
            log.info("Thread was interrupted caused by: " + e.getMessage());
        }
        return result;
    }

    /**
     * Checks {@link ProxyStatus} and proxy speed
     * @param iProxy proxy to check
     * @param url destination address
     * @return {@link Pair} that contains {@link ProxyStatus} and proxy speed
     */
    public static Pair checkProxy(IProxy iProxy, String url) {
        HttpClient httpClient = getHttpClient(iProxy);

        HttpRequest httpRequest = HttpRequest
                .newBuilder()
                .GET()
                .timeout(Duration.ofMillis(10_000))
                .uri(URI.create(url))
                .build();

        Pair<ProxyStatus, String> result;
        String defaultSpeed = "-1 (ms)";
        int statusCode = -1;
        try {
            long start = System.currentTimeMillis();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            result = Pair.of((httpResponse.statusCode() == 200 || httpResponse.statusCode() == 302) ? ProxyStatus.VALID : ProxyStatus.INVALID,
                    String.format("%d (ms)", System.currentTimeMillis() - start));
            statusCode = httpResponse.statusCode();
            log.info(String.format("Proxy: {%s} -- %s [%d], for host: %s", iProxy.getHost(), result, statusCode, url));
        } catch (IOException e) {
            result = Pair.of(ProxyStatus.INVALID, defaultSpeed);
            log.info(String.format("Proxy: {%s} -- %s [%d], for host: %s", iProxy.getHost(), result, statusCode, url));
        } catch (InterruptedException e) {
            result = Pair.of(ProxyStatus.INVALID, defaultSpeed);
            log.info("Thread was interrupted caused by: " + e.getMessage());
        }
        return result;
    }

    private static HttpClient getHttpClient(IProxy iProxy) {
        return HttpClient.newBuilder()
                .proxy(ProxySelector.of(new InetSocketAddress(iProxy.getHost(), iProxy.getPort())))
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(iProxy.getLogin(), iProxy.getPass().toCharArray());
                    }
                })
                .build();
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
