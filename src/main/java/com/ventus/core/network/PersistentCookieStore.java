package com.ventus.core.network;

import com.ventus.core.models.Cookie;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {

//            int length = 0;
//
//            Scanner scanner = new Scanner(new FileInputStream(path));
//            while (scanner.hasNextLine()) {
//                String s = scanner.nextLine();
//                if (s.contains(login)) {
//                    String[] strings = s.split(":");
//                    cookieString = strings[1];
//                    break;
//                } else {
//                    length += s.getBytes().length;
//                }
//            }
//
//            length += (login + ":").getBytes().length;
//
//            objectInputStream.skipBytes(length);
            ArrayList<Cookie> cookies = (ArrayList<Cookie>) objectInputStream.readObject();

            cookies.forEach(cookie -> httpCookieList.add(convertToHttpCookie(cookie)));
            cookies.forEach(cookie -> store.add(uri, convertToHttpCookie(cookie)));

            System.out.println(httpCookieList);
            System.out.println(cookies);

//            if (cookieString != null) {
//                Arrays.stream(cookieString.split("; ")).forEach(s -> {
//                    String[] parsed = s.split("=", 2);
//                    HttpCookie cookie = new HttpCookie(parsed[0], parsed[1]);
//                    cookie.setVersion(0);
//                    httpCookieList.add(cookie);
//                });
//            }

        } catch (IOException | ClassNotFoundException e) {
            log.error("Error while reading cookies from file: " + e.getMessage());
        }

        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    @Override
    public void run() {
        // writing cookies in store to persistent storage
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            ArrayList<Cookie> cookies = new ArrayList<>();

            getCookies().forEach(httpCookie -> cookies.add(convertToCookieModel(httpCookie)));
            objectOutputStream.writeObject(cookies);
            objectOutputStream.flush();

        } catch (IOException e) {
            log.error("Error while writing cookies down in file");
        }
    }

    private Cookie convertToCookieModel(HttpCookie httpCookie) {
        return Cookie
                .builder()
                .comment(httpCookie.getComment())
                .commentURL(httpCookie.getCommentURL())
                .domain(httpCookie.getDomain())
                .httpOnly(httpCookie.isHttpOnly())
                .maxAge(httpCookie.getMaxAge())
                .name(httpCookie.getName())
                .path(httpCookie.getPath())
                .portlist(httpCookie.getPortlist())
                .secure(httpCookie.getSecure())
                .version(httpCookie.getVersion())
                .toDiscard(httpCookie.getDiscard())
                .value(httpCookie.getValue())
                .build();
    }

    private HttpCookie convertToHttpCookie(Cookie cookie) {
        HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());

        httpCookie.setComment(cookie.getComment());
        httpCookie.setCommentURL(cookie.getCommentURL());
        httpCookie.setDomain(cookie.getDomain());
        httpCookie.setHttpOnly(cookie.isHttpOnly());
        httpCookie.setMaxAge(cookie.getMaxAge());
        httpCookie.setPath(cookie.getPath());
        httpCookie.setPortlist(cookie.getPortlist());
        httpCookie.setSecure(cookie.isSecure());
        httpCookie.setVersion(cookie.getVersion());
        httpCookie.setDiscard(cookie.isToDiscard());

        return httpCookie;
    }

    @Override
    public void	add(URI uri, HttpCookie cookie) {
        List<HttpCookie> httpCookies = store.get(uri);
        boolean updated = false;
        for (HttpCookie savedCookie : httpCookies) {
            if (savedCookie.getName() == cookie.getName()) {
                if (savedCookie.hasExpired()) {
                    store.add(uri, cookie);
                    log.info(String.format("ADDED: %s|%s --> %s == %s", uri, cookie.getDomain(), cookie.getName(), cookie.getValue()));
                    updated = true;
                }
            }
        }
//        if (!updated) store.add(uri, cookie);
//        store.add(uri, cookie);
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