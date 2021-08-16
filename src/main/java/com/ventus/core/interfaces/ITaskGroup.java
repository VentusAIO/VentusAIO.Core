package com.ventus.core.interfaces;

import com.ventus.core.models.AccountManager;
import com.ventus.core.models.ProfileManager;
import com.ventus.core.models.TaskGroupStatus;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.proxy.ProxyManager;
import com.ventus.core.task.RequestModule;
import com.ventus.core.task.TasksFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    TaskGroupStatus getStatus();

    void setStatus(TaskGroupStatus status);

    ExecutorService executorService = Executors.newCachedThreadPool();

    default List<Future<?>> start() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

        List<Future<?>> futures = new LinkedList<>();
        List<Callable<Map<?, ?>>> tasks = new ArrayList<>();

        ProxyManager proxyManager = new ProxyManager();
        proxyManager.addProxyList(getProxies());

        ProfileManager profileManager = new ProfileManager(getProfiles());
        AccountManager accountManager = new AccountManager(getAccounts());


        for (int i = 0; i < getAmount(); i++) {
            RequestModule task = TasksFactory.getTask(getTasksType());


            task.configureProxy(proxyManager);
            task.setProfileManger(profileManager);
            task.setAccountManger(accountManager);


            task.setItemId(getItemId());
            task.setFilter(getFilter());
            task.setSizes(getSizes());
            tasks.add(task);
        }
        for (Callable<Map<?, ?>> task : tasks) {
            Future<Map<?, ?>> future = executorService.submit(task);
            futures.add(future);
        }
        executorService.shutdown();
        return futures;
    }

    default void stop() {
        executorService.shutdownNow();
    }
}
