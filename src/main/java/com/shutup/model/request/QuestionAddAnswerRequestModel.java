package com.shutup.model.request;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by shutup on 2017/1/28.
 */
public class QuestionAddAnswerRequestModel {
    private Long userId;
    @NotEmpty
    private String answer;

    protected QuestionAddAnswerRequestModel() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
