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
//    default List<ITask> start() {
//        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
//        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
//        getExecutorService().shutdownNow();
//        setExecutorService(Executors.newCachedThreadPool());
//
//        ProxyManager proxyManager = new ProxyManager();
//        proxyManager.addProxyList(getProxies());
//
//        ProfileManager profileManager = null;
//        AccountManager accountManager = null;
//        if (getProfiles() != null) {
//            profileManager = new ProfileManager(getProfiles());
//        }
//        if (getAccounts() != null) {
//            accountManager = new AccountManager(getAccounts());
//        }
//
//        List<ITask> task_list = new LinkedList<>();
//        for (int i = 0; i < getAmount(); i++) {
//            RequestModule task = TasksFactory.getTask(getTasksType());
//
//
//            task.configureProxy(proxyManager);
//
//            if (profileManager != null) task.setProfileManger(profileManager);
//            if (accountManager != null) task.setAccountManger(accountManager);
//
//            task.setItemId(getItemId());
//            task.setFilter(getFilter());
//            task.setSizes(getSizes());
//            task.setUser_logs(itask.getLogs());
//            Future<Map<?, ?>> future = getExecutorService().submit(task);
//            itask.setFuture(future);
//            task_list.add(itask);
//        }
//        getExecutorService().shutdown();
//        return task_list;
//    }

    default void stop() {
        getExecutorService().shutdownNow();
    }
}
