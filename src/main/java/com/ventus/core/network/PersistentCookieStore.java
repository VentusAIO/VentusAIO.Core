package com.ventus.core.network;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class PersistentCookieStore implements CookieStore, Runnable {
    private String path;
    private String login;
    CookieStore store;

    @Builder
    public PersistentCookieStore(String login, String path, URI uri) {
        this.login = login;
        this.path = path;

        store = new CookieManager().getCookieStore();

        // reading in cookies from persistent storage
        List<HttpCookie> httpCookieList = new LinkedList<>();
        String cookieString = null;

        try{
            Scanner scanner = new Scanner(new FileInputStream(path));
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if (s.contains(login)) {
                    String[] strings = s.split(":");
                    cookieString = strings[1];
                    break;
                }
            }

            if (cookieString != null) {
                Arrays.stream(cookieString.split("; ")).forEach(s -> {
                    String[] parsed = s.split("=", 2);
                    HttpCookie cookie = new HttpCookie(parsed[0], parsed[1]);
                    cookie.setVersion(0);
                    httpCookieList.add(cookie);
                });
            }

        } catch (IOException e) {
            log.error("Error while reading cookies from file");
        }
        if (cookieString != null) {
            httpCookieList.forEach(cookie -> add(uri, cookie));
        }

        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    public void run() {
        // writing cookies in store to persistent storage
        try {
            StringBuilder stringBuilder = new StringBuilder();
            this.getCookies().forEach(httpCookie -> stringBuilder.append(httpCookie.toString() + "; "));
            stringBuilder.setLength(stringBuilder.length() - 2);

            List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of(path), StandardCharsets.UTF_8));
            boolean flag = false;

            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).contains(login)) {
                    fileContent.set(i, login + ":" + stringBuilder.toString());
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                fileContent.add(login + ":" + stringBuilder.toString());
            }

            Files.write(Path.of(path), fileContent, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error while writing cookies down in file");
        }
    }

    public void	add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie);
    }

    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    public List<URI> getURIs() {
        return store.getURIs();
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    public boolean removeAll()  {
        return store.removeAll();
    }
}