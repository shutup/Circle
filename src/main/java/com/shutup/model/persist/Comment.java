package com.shutup.model.persist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shutup on 2017/4/2.
 */
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String comment;

    @OneToOne
    private User user;

    @OneToOne
    private User replyUser;

    private boolean isReply;



    /** Model created at timestamp. */

    @CreationTimestamp
    private Date createdAt;

    /** Model updated at timestamp. */

    @UpdateTimestamp
    private Date updatedAt;

    @OneToMany(targetEntity = User.class)
    private List<User> agreedUsers = new ArrayList<>();
    @OneToMany(targetEntity = User.class)
    private List<User> disagreedUsers = new ArrayList<>();

    public Comment(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getAgreedUsers() {
        return agreedUsers;
    }

    public void setAgreedUsers(List<User> agreedUsers) {
        this.agreedUsers = agreedUsers;
    }

    public List<User> getDisagreedUsers() {
        return disagreedUsers;
    }

    public void setDisagreedUsers(List<User> disagreedUsers) {
        this.disagreedUsers = disagreedUsers;
    }

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }
}
