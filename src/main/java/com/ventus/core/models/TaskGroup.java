package com.ventus.core.models;

import com.ventus.core.interfaces.*;
import com.ventus.core.network.AvailabilityFilters;
import com.ventus.core.proxy.ProxyManager;
import com.ventus.core.task.RequestModule;
import com.ventus.core.task.TasksFactory;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Data
public class TaskGroup implements ITaskGroup {
    Class<? extends RequestModule> tasksType = null;
    AvailabilityFilters filter = null;
    TaskGroupStatus status;
    ExecutorService executorService = Executors.newCachedThreadPool();
    private Long id;
    private String name;
    private String itemId;
    private int amount;
    private List<IProfile> profiles = new ArrayList<>();
    private List<IProxy> proxies = new ArrayList<>();
    private List<IAccount> accounts = new ArrayList<>();

    @Override
    public String[] getSizes() {
        return new String[0];
    }

    @Override
    public List<ITask> start() {
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
        getExecutorService().shutdownNow();
        setExecutorService(Executors.newCachedThreadPool());

        ProxyManager proxyManager = new ProxyManager();
        proxyManager.addProxyList(getProxies());

        ProfileManager profileManager = null;
        AccountManager accountManager = null;
        if (getProfiles() != null) {
            profileManager = new ProfileManager(getProfiles());
        }
        if (getAccounts() != null) {
            accountManager = new AccountManager(getAccounts());
        }

        List<ITask> task_list = new LinkedList<>();
        for (int i = 0; i < getAmount(); i++) {
            RequestModule task = TasksFactory.getTask(getTasksType());


            task.configureProxy(proxyManager);

            if (profileManager != null) task.setProfileManger(profileManager);
            if (accountManager != null) task.setAccountManger(accountManager);

            ITask itask = new Task();
            task.setItemId(getItemId());
            task.setFilter(getFilter());
            task.setSizes(getSizes());
            task.setUser_logs(itask.getLogs());
            Future<Map<?, ?>> future = getExecutorService().submit(task);
            itask.setFuture(future);
            task_list.add(itask);
        }
        getExecutorService().shutdown();
        return task_list;
    }
}
