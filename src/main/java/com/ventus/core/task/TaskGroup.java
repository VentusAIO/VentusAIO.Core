package com.ventus.core.task;

import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.ITaskGroup;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.proxy.ProxyManager;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class TaskGroup implements ITaskGroup {
    private String itemId;
    private int tasksAmount;
    private List<IProfile> profiles = new ArrayList<>();
    private List<IProxy> proxies = new ArrayList<>();
    private Class<? extends RequestModule> tasksType;
    private AvailabilityFilters filter;
    private String[] sizes;

    public TaskGroup() {

    }

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < tasksAmount; i++) {
            ProxyManager proxyManager = new ProxyManager();
            proxyManager.addProxyList(proxies);
            RequestModule task = TasksFactory.getTask(tasksType);
            task.configureProxy(proxyManager);
            task.setProfileGroup(profiles);
            task.setItemId(itemId);
            task.setFilter(filter);
            task.setSizes(sizes);
            executorService.submit(task);
        }
    }
}
