package com.ventus.core.models;

import com.ventus.core.interfaces.ITask;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.Future;

@Data
public class Task implements ITask {
    private String status;
    private String message;
    private StringBuilder logs;
    private Future<Map<?, ?>> future;


    public Task() {
        this.status = "run";
        this.message = "null";
        this.logs = new StringBuilder();
    }
}
