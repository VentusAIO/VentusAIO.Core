package com.ventus.core.models;

import com.ventus.core.interfaces.IAccount;

import java.util.LinkedList;
import java.util.List;

public class AccountManager {
    private final LinkedList<IAccount> accounts;

    public AccountManager(List<IAccount> accounts){
        this.accounts = new LinkedList<>(accounts);
    }

    public IAccount getAccount(){
        if(accounts.isEmpty()) return null;
        return accounts.pop();
    }
}
