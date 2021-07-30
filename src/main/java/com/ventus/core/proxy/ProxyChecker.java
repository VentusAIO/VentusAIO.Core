package com.ventus.core.proxy;

import com.ventus.core.interfaces.IProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProxyChecker {
    private static final List<IProxy> proxyPairs = new ArrayList<>();

    private ProxyChecker() {

    }

    protected static synchronized void callback(String message, IProxy proxyPair) {
        proxyPairs.add(proxyPair);
        log.info(message);
    }

    public static List<IProxy> check(List<IProxy> proxyPairs, String host) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (IProxy proxyPair : proxyPairs) {
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
