package com.ventus.core.interfaces;

import java.util.Map;

public interface IAuth {
    Map<String, String> auth(String login, String pass);
    String auth(Map<String, String> options);
}