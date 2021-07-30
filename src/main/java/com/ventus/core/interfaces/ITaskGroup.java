package com.ventus.core.interfaces;

import com.ventus.core.task.RequestModule;

import java.util.List;

public interface ITaskGroup {
    Class<? extends RequestModule> getTasksType();

    void setTasksType(Class<? extends RequestModule> tasksType);

    List<IProfile> getProfiles();

    void setProfiles(List<IProfile> profiles);

    List<IProxy> getProxies();

    void setProxies(List<IProxy> proxies);

    String getItemId();

    void setItemId(String id);

    int getTasksAmount();

    void setTasksAmount(int amount);

    void start();
}
