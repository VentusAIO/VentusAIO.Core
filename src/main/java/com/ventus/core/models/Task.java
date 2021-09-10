package com.ventus.core.models;

import com.ventus.core.interfaces.ITask;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.Future;

@Data
public class Task implements ITask {
    private TaskStatus status;
    private boolean checked = false;
    private String message;
    private StringBuilder logs;
    private Future<Map<?, ?>> future;


    public Task() {
        this.status = TaskStatus.IN_PROGRESS;
        this.message = "null";
        this.logs = new StringBuilder();
    }
}
