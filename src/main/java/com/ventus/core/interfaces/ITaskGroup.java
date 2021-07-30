package com.ventus.core.interfaces;

import com.ventus.core.task.RequestModule;

import java.util.List;

public interface ITaskGroup {
    void setTaskType(Class<? extends RequestModule> tasksType);

    void setProfiles(List<IProfile> profiles);

    void setProxies(List<IProxy> proxies);

    void setItemId(String id);

    void setTasksAmount(int amount);

    void start();
}
