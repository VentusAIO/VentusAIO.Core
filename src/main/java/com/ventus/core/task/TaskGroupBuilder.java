package com.ventus.core.task;

import com.ventus.core.profile.ProfileGroup;
import com.ventus.core.proxy.ProxyManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TaskGroupBuilder {

    private List<RequestModule> tasks = new CopyOnWriteArrayList<>();
    private ProxyManager proxyManager;
    private int tasksAmount;
    private Class<? extends RequestModule> tasksType;
    private ProfileGroup profileGroup;
    private String pid;

    public static TaskGroupBuilder create() {
        return new TaskGroupBuilder();
    }

    public TaskGroupBuilder setTasks (List<RequestModule> tasks) {
        this.tasks = tasks;
        return this;
    }

    public TaskGroupBuilder setProxyManager (ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
        return this;
    }

    public TaskGroupBuilder setTasksAmount(int tasksAmount) {
        this.tasksAmount = tasksAmount;
        return this;
    }

    public TaskGroupBuilder setTasksType(Class<? extends RequestModule> tasksType) {
        this.tasksType = tasksType;
        return this;
    }

    public TaskGroupBuilder setProfileGroup(ProfileGroup profileGroup) {
        this.profileGroup = profileGroup;
        return this;
    }

    public TaskGroupBuilder setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public TaskGroup build() {
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setProfileGroup(profileGroup);
        taskGroup.setProxyManager(proxyManager);
        taskGroup.setTasks(tasks);
        taskGroup.setTasksType(tasksType);
        taskGroup.addTasks(tasksAmount);
        taskGroup.setPid(pid);
        return taskGroup;
    }
}
