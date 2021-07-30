package com.ventus.core.proxy;

import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ProxyConfigurer {

    // TODO: add file selection or load it from db or something like this.
    @Setter
    private static String proxyConfigFileName = "src/main/resources/Proxy/proxyConfig.txt";

    public static ProxyManager configure(ProxyManager proxyManager, String host) {

        List<ProxyPair> pairs = new LinkedList<>();

        try {
            Scanner scanner = new Scanner(new FileInputStream(proxyConfigFileName));
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                String[] strings = s.split(":", -1);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(strings[0], Integer.parseInt(strings[1])));
                Credentials credentials = new Credentials();
                credentials.setUsername(strings[2]);
                credentials.setPassword(strings[3]);
                ProxyPair pair = new ProxyPair(proxy, credentials);
                pairs.add(pair);
            }
            ProxyChecker.check(pairs, host);
            proxyManager.addProxyList(pairs);
        } catch (FileNotFoundException e) {
            System.out.println("File with name: " + proxyConfigFileName + " was not found");
            e.printStackTrace();
        }
        return proxyManager;
    }
}
