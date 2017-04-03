package com.shutup.controller;

import com.shutup.exception.CustomeException;
import com.shutup.model.request.UserRegisterRequestModel;
import com.shutup.model.persist.User;
import com.shutup.model.request.UserLoginRequestModel;
import com.shutup.model.response.UserLoginResponseModel;
import com.shutup.repo.UserRepo;
import com.shutup.service.UserService;
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

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/register"},method = RequestMethod.POST)
    public ResponseEntity<User> register(@RequestBody UserRegisterRequestModel registerUserRequestModel) {
        User user = userService.Register(registerUserRequestModel);
        if (user != null) {
            return ResponseEntity.ok(user);
        }else {
            throw new CustomeException("already exist username",false, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(path = {"/login"},method = RequestMethod.POST)
    public ResponseEntity<UserLoginResponseModel> login(@RequestBody UserLoginRequestModel userLoginRequestModel) {
        UserLoginResponseModel userLoginResponseModel = userService.Login(userLoginRequestModel);
        if (userLoginResponseModel != null) {
            return ResponseEntity.ok(userLoginResponseModel);
        }else {
            throw new CustomeException("login failed",false, HttpStatus.NOT_FOUND);
        }
    }
}
