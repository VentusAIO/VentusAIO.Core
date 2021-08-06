package com.ventus.core.models;

import com.ventus.core.interfaces.IAccount;
import com.ventus.core.network.PersistentCookieStore;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.net.URI;

@Data
@Builder
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

    @Override
    public PersistentCookieStore getCookieStore() {
        return cookies;
    }
}
