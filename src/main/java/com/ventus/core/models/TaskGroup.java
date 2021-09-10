package com.ventus.core.models;

import com.ventus.core.interfaces.*;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.task.RequestModule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class TaskGroup implements ITaskGroup {
    private Long id;
    private String name;
    private String itemId;
    private int amount;
    private List<IProfile> profiles = new ArrayList<>();
    private List<IProxy> proxies = new ArrayList<>();
    private List<IAccount> accounts = new ArrayList<>();
    Class<? extends RequestModule> tasksType = null;
    AvailabilityFilters filter = null;
    TaskGroupStatus status;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public String[] getSizes() {
        return new String[0];
    }

    @Override
    public List<ITask> start() {
        return null;
    }
}
