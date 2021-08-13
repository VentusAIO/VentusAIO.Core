package com.ventus.core.models;

import com.ventus.core.interfaces.IAccount;
import com.ventus.core.network.PersistentCookieStore;
import lombok.Builder;
import lombok.Data;

import java.net.CookieStore;
import java.net.URI;

@Data
public class Account implements IAccount {
    private String login;
    private String pass;
    private PersistentCookieStore cookies;

    @Builder
    public Account(String login, String pass, String path, URI uri) {
        this.login = login;
        this.pass = pass;
        cookies = PersistentCookieStore.builder().login(login).path(path).uri(uri).build();
    }
    // idk what is going on here
    public Account(String login, String path, PersistentCookieStore cookies) {
        this.login = login;
        this.pass = "";
        this.cookies = cookies;
    }

    @Override
    public PersistentCookieStore getCookieStore() {
        return cookies;
    }
}
