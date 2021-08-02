package com.ventus.core.models;

import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.ITaskGroup;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.task.RequestModule;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class TaskGroup implements ITaskGroup {
    private Long id;
    private String name;
    private String itemId;
    private int amount;
    private List<IProfile> profiles;
    private List<IProxy> proxies;
    Class<? extends RequestModule> tasksType = null;
    AvailabilityFilters filter = null;
    ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public String[] getSizes() {
        return new String[0];
    }
}
