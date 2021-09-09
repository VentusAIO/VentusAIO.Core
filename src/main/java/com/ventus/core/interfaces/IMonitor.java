package com.ventus.core.interfaces;

import java.util.Map;

public interface IMonitor {
    boolean monitor();

    Map<String, String> checkout();

    default Map<String, String> call() throws InterruptedException {
        while (!monitor()) {
            Thread.sleep(2000);
        }
        return checkout();
    }
}
