package com.game.server.proto;

/**
 * Created by Administrator on 2020/7/4.
 */

public class ProtoLoginR {

    private String account;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
