package com.ventus.core.interfaces;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public interface ITask {
    String getStatus();

    void setStatus(String status);

    String getMessage();

    void setMessage(String message);

    StringBuilder getLogs();

    Future<Map<?, ?>> getFuture();

    void setFuture(Future<Map<?, ?>> future);

    default void update() throws InterruptedException {
        if (getFuture().isCancelled()) {
            setStatus("cancel");
        }
        if (getStatus().equals("run") && getFuture().isDone()) {
            try {
                Map<String, String> map = (Map<String, String>) getFuture().get();
                if (map.get("status") != null) {
                    setStatus(map.get("status"));
                } else {
                    setStatus("null");
                }
                if (map.get("message") != null) {
                    setMessage(map.get("message"));
                } else {
                    setMessage("null");
                }
            } catch (ExecutionException e) {
                setStatus("cancel");
            }
        }
    }
}
