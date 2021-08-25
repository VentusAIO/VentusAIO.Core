package com.ventus.core.interfaces;

import com.ventus.core.network.PersistentCookieStore;

public interface IAccount {
    String getLogin();
    void setLogin(String login);
    String getPass();
    void setPass(String pass);
    String getCookies();
}