package com.shutup.model.request;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by shutup on 2017/4/2.
 */
public class UserLoginRequestModel {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;


    protected UserLoginRequestModel(){

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
