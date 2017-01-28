package com.shutup.model.request;

/**
 * Created by shutup on 2017/1/28.
 */
public class CreateAskRequestModel {
    private Long userId;
    private String question;

    protected CreateAskRequestModel(){}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
