package com.ventus.core.interfaces;

import com.ventus.core.models.TaskStatus;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public interface ITask {
    TaskStatus getStatus();

    void setStatus(TaskStatus status);

    boolean isChecked();

    void setChecked(boolean checked);

    String getMessage();

    void setMessage(String message);

    StringBuilder getLogs();

    Future<Map<?, ?>> getFuture();

    void setFuture(Future<Map<?, ?>> future);

    default void update() throws InterruptedException {
        if (getFuture().isCancelled()) {
            setStatus(TaskStatus.CANCEL);
        }
        if (getStatus().equals(TaskStatus.IN_PROGRESS) && getFuture().isDone()) {
            try {
                Map<String, Object> map = (Map<String, Object>) getFuture().get();
                if (map.get("status") != null) {
                    setStatus((TaskStatus) map.get("status"));
                } else {
                    setStatus(TaskStatus.ERROR);
                }
                if (map.get("message") != null) {
                    setMessage((String) map.get("message"));
                } else {
                    setMessage("null");
                }
            } catch (ExecutionException e) {
                setStatus(TaskStatus.CANCEL);
            }
        }
    }
}
