package com.ventus.core.task;

import com.ventus.core.interfaces.IProfile;
import com.ventus.core.interfaces.IProxy;
import com.ventus.core.interfaces.ITaskGroup;
import com.ventus.core.proxy.ProxyManager;
import lombok.Builder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
public class TaskGroup implements ITaskGroup {
    private String pid;
    private int tasksAmount;
    private List<IProfile> profiles = new LinkedList<>();
    private List<IProxy> proxies = new LinkedList<>();
    private Class<? extends RequestModule> tasksType;

    TaskGroup() {

    }

    @Override
    public void setTaskType(Class<? extends RequestModule> tasksType) {
        this.tasksType = tasksType;
    }

    @Override
    public void setProfiles(List<IProfile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public void setProxies(List<IProxy> proxies) {
        this.proxies = proxies;
    }

    @Override
    public void setItemId(String id) {
        this.pid = id;
    }

    @Override
    public void setTasksAmount(int amount) {
        this.tasksAmount = amount;
    }

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < tasksAmount; i++) {
            ProxyManager proxyManager = new ProxyManager();
            proxyManager.addProxyList(proxies);
            RequestModule task = TasksFactory.getTask(tasksType);
            task.configureProxy(proxyManager);
            task.profileGroup = profiles;
            task.pid = pid;
            executorService.submit(task);
        }
    }
}
