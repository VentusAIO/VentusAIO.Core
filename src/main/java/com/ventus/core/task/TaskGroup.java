package com.ventus.core.task;

import com.ventus.core.profile.ProfileGroup;
import com.ventus.core.proxy.ProxyManager;
import com.ventus.core.proxy.ProxyPair;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskGroup {

    private List<RequestModule> tasks = new CopyOnWriteArrayList<>();
    private ProxyManager proxyManager;
    private Class<? extends RequestModule> tasksType;
    private int tasksAmount;
    private ProfileGroup profileGroup;
    private String pid;

    TaskGroup() {

    }

    public List<Future<?>> start() {
        List<Future<?>> futures = new LinkedList<>();

        for (int i = 0; i < tasksAmount; i++) {
            RequestModule task = TasksFactory.getTask(tasksType);
            task.configureProxy(proxyManager);
            task.profileGroup = this.profileGroup;
            task.pid = pid;
            tasks.add(task);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (Runnable task : tasks) {
            Future<?> future = executorService.submit(task);
            futures.add(future);
        }
        return futures;
    }

    public void addProxyList(List<ProxyPair> proxies) {
        if (proxies.isEmpty()) return;
        proxyManager = new ProxyManager();
        proxyManager.addProxyList(proxies);
    }

    // TODO: refactor returning value

    public void setTasks(List<RequestModule> tasks) {
        this.tasks = tasks;
    }

    public void setProxyManager(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    public void addTasks(int tasksAmount) {
        this.tasksAmount = tasksAmount;
    }

    public void setTasksType(Class<? extends RequestModule> tasksType) {
        this.tasksType = tasksType;
    }

    public void setProfileGroup(ProfileGroup profileGroup) {
        this.profileGroup = profileGroup;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
