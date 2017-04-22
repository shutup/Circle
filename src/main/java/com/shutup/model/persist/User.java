package com.shutup.model.persist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by shutup on 2017/1/28.
 */
@Entity
@JsonIgnoreProperties(value = {"password"})
public class User {
    @Column(unique = true)
    @Length(min = 2,max = 10)
    private String username;
    private String password;

    /** Model created at timestamp. */

    @CreationTimestamp
    private Date createdAt;

    /** Model updated at timestamp. */

    @UpdateTimestamp
    private Date updatedAt;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    protected User(){

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
