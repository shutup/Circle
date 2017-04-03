package com.shutup.model.persist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by shutup on 2017/4/2.
 */
@Entity
public class UserStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    /** Model created at timestamp. */
    @CreationTimestamp
    private Date createdAt;

    /** Model updated at timestamp. */
    @UpdateTimestamp
    private Date updatedAt;

    @Lob
    private String token;
    @OneToOne(targetEntity = User.class)
    private User user;

    protected UserStatus(){}

    public UserStatus(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
