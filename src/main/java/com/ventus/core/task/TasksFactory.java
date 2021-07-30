package com.ventus.core.task;

import lombok.SneakyThrows;

public class TasksFactory {
    @SneakyThrows
    public static <T extends RequestModule> T getTask(Class<? extends RequestModule> type) {
        T requestModule = (T) type.getDeclaredConstructor().newInstance();
        //TODO: add some pre config if needed.
        return requestModule;
    }
}
