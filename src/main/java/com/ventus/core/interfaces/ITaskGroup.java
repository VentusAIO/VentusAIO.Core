package com.ventus.core.interfaces;

import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.task.RequestModule;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface ITaskGroup {
    AvailabilityFilters getFilter();

    void setFilter(AvailabilityFilters filter);

    Class<? extends RequestModule> getTasksType();

    List<IProfile> getProfiles();

    List<IAccount> getAccounts();

    List<IProxy> getProxies();

    String getItemId();

    String[] getSizes();

    int getAmount();

    ExecutorService getExecutorService();

    void setExecutorService(ExecutorService executorService);

    List<? extends ITask> start();

    default void stop() {
        getExecutorService().shutdownNow();
    }
}
