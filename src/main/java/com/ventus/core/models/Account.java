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
    private String cookies;

    @Builder
    public Account(String login, String pass, String cookies) {
        this.login = login;
        this.pass = pass;
        this.cookies = cookies;
    }
}
