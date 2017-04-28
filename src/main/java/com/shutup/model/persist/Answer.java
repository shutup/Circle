package com.shutup.model.persist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shutup on 2017/1/28.
 */
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String answer;

    @OneToOne
    private User user;

    @OneToMany(targetEntity = Comment.class)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(targetEntity = User.class)
    private List<User> agreedUsers = new ArrayList<>();
    @ManyToMany(targetEntity = User.class)
    private List<User> disagreedUsers = new ArrayList<>();

    /** Model created at timestamp. */

    @CreationTimestamp
    private Date createdAt;

    /** Model updated at timestamp. */

    @UpdateTimestamp
    private Date updatedAt;

    protected Answer(){}

    public Answer(String answer, User user) {
        this.answer = answer;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
}
