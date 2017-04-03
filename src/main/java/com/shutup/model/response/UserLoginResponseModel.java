package com.shutup.model.response;

/**
 * Created by shutup on 2017/4/2.
 */
public class UserLoginResponseModel {
    private String username;
    private String token;


    protected UserLoginResponseModel(){

    }

    public UserLoginResponseModel(String username, String token) {
        this.username = username;
        this.token = token;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
