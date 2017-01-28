package com.shutup.controller;

import com.shutup.exception.CustomeException;
import com.shutup.model.request.RegisterUserRequestModel;
import com.shutup.model.persist.User;
import com.shutup.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shutup on 2017/1/28.
 */
@RestController
@RequestMapping(path = {"/user"})
public class UserController {

    @Autowired
    UserRepo userRepo;

    @RequestMapping(path = {"/register"},method = RequestMethod.POST)
    public ResponseEntity<User> register(@RequestBody RegisterUserRequestModel registerUserRequestModel) {
        User user = userRepo.findByUsername(registerUserRequestModel.getUsername());
        if (user == null) {
            user = userRepo.save(new User(registerUserRequestModel.getUsername(),registerUserRequestModel.getPassword()));
            return ResponseEntity.ok(user);
        }else {
            throw new CustomeException("already exist username",false, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = {"/login"},method = RequestMethod.POST)
    public void login() {

    }
}
