package com.shutup.model.request;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by shutup on 2017/1/28.
 */
public class CreateQuestionRequestModel {
    private Long userId;
    @NotEmpty
    private String question;

    protected CreateQuestionRequestModel(){}

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
