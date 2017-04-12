package com.shutup.model.request;

import com.shutup.model.persist.Comment;
import com.shutup.model.persist.User;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by shutup on 2017/4/2.
 */
public class QuestionAddCommentRequestModel {
    @NotEmpty
    private String comment;

    protected QuestionAddCommentRequestModel() {}

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Comment createComment(User user) {
        Comment comment = new Comment();
        comment.setComment(this.comment);
        comment.setUser(user);
        return comment;
    }

    public Comment createReplyComment(User user,User replyUser) {
        Comment comment = new Comment();
        comment.setComment(this.comment);
        comment.setUser(user);
        comment.setReply(true);
        comment.setReplyUser(replyUser);
        return comment;
    }

}
