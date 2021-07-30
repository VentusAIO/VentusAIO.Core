package com.ventus.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProxyChecker{

    private static final List<ProxyPair> proxyPairs = new ArrayList<>();

    private ProxyChecker() {

    }

    protected static synchronized void callback(String message, ProxyPair proxyPair) {
        proxyPairs.add(proxyPair);
        log.info(message);
    }

    public static List<ProxyPair> check(List<ProxyPair> proxyPairs, String host) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (ProxyPair proxyPair : proxyPairs) {
            executorService.submit(new ProxyCheckerTask(proxyPair, host));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return proxyPairs;
    }
}
