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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String question;

    @OneToMany(targetEntity = Answer.class)
    private List<Answer> answers = new ArrayList<>();

    @OneToOne
    private User user;

    /** Model created at timestamp. */

    @CreationTimestamp
    private Date createdAt;

    /** Model updated at timestamp. */

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToMany(targetEntity = User.class)
    private List<User> agreedUsers = new ArrayList<>();
    @ManyToMany(targetEntity = User.class)
    private List<User> disagreedUsers = new ArrayList<>();

    public Question() {
    }

    public Question(String question, User user) {
        this.question = question;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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
