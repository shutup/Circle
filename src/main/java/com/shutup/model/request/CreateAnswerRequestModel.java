package com.shutup.model.request;

/**
 * Created by shutup on 2017/1/28.
 */
public class CreateAnswerRequestModel {
    private Long userId;
    private String answer;

    protected CreateAnswerRequestModel() {}

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
