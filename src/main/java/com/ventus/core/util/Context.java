package com.ventus.core.util;

import com.ventus.core.interfaces.IProxy;
import com.ventus.core.models.TaskGroup;
import com.ventus.core.proxy.ProxyManagerImpl;
import com.ventus.core.proxy.ProxyManagerFactory;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class Context {
    private static final HashMap<TaskGroup, ProxyManagerImpl> serviceInstanceToManager = new HashMap<>();

    public static void configureContext(String packageToScan) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setScanners();
        Reflections reflections = new Reflections(packageToScan);
        Set<Class<? extends IProxy>> subTypesOf = reflections.getSubTypesOf(IProxy.class);
        Set<String> allTypes = reflections.getAllTypes();
        System.out.println(subTypesOf);
//        System.out.println(allTypes);
    }

    public synchronized static ProxyManagerImpl getProxyManager (TaskGroup taskGroupInstance) {
        ProxyManagerImpl proxyManager = serviceInstanceToManager.get(taskGroupInstance);
        if (proxyManager == null) {
            proxyManager = ProxyManagerFactory.createProxyManager(taskGroupInstance.getHost());
            serviceInstanceToManager.put(taskGroupInstance, proxyManager);
        }
        return proxyManager;
    }
}
