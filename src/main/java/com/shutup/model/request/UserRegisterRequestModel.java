package com.shutup.model.request;

/**
 * Created by shutup on 2017/1/28.
 */
public class UserRegisterRequestModel {
    private String username;
    private String password;

    protected UserRegisterRequestModel(){

    }

    public UserRegisterRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
