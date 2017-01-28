package com.shutup.model.response;

/**
 * Created by shutup on 2017/1/28.
 */
public class RestMessage {
    private String msg;
    private boolean isSuccess;

    protected RestMessage (){

    }

    public RestMessage(String msg, boolean isSuccess) {
        this.msg = msg;
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
