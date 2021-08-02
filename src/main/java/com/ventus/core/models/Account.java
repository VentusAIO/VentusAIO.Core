package com.ventus.core.models;

import com.ventus.core.interfaces.IAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account implements IAccount {
    private String login;
    private String pass;
    private String cookies;
}
