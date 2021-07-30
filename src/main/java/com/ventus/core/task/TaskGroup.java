package com.ventus.core.task;

import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.ITaskGroup;
import com.ventus.core.proxy.ProxyManager;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class TaskGroup implements ITaskGroup {
    private String itemId;
    private int tasksAmount;
    private List<IProfile> profiles = new LinkedList<>();
    private List<IProxy> proxies = new LinkedList<>();
    private Class<? extends RequestModule> tasksType;

    public TaskGroup() {

    }

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < tasksAmount; i++) {
            ProxyManager proxyManager = new ProxyManager();
            proxyManager.addProxyList(proxies);
            RequestModule task = TasksFactory.getTask(tasksType);
            task.configureProxy(proxyManager);
            task.profileGroup = profiles;
            task.itemId = itemId;
            executorService.submit(task);
        }
    }
}
