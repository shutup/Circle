package com.shutup.service;

import com.shutup.exception.CustomeException;
import com.shutup.model.persist.User;
import com.shutup.model.persist.UserStatus;
import com.shutup.model.request.UserLoginRequestModel;
import com.shutup.model.request.UserRegisterRequestModel;
import com.shutup.model.response.UserLoginResponseModel;
import com.shutup.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Created by shutup on 2017/4/2.
 */
@Service
public class UserService {

    @Autowired
    UserStatusService userStatusService;
    @Autowired
    UserRepo userRepo;


    public User Register(UserRegisterRequestModel userRegisterRequestModel) {
        User user = userRepo.findByUsername(userRegisterRequestModel.getUsername());
        if (user == null) {
            user = userRepo.save(new User(userRegisterRequestModel.getUsername(), userRegisterRequestModel.getPassword()));
            return user;
        } else {
            return null;
        }
    }

    public UserLoginResponseModel Login(UserLoginRequestModel userLoginRequestModel) {
        User user = userRepo.findByUsername(userLoginRequestModel.getUsername());
        if (user!= null && user.getPassword().contentEquals(userLoginRequestModel.getPassword())) {
            UserStatus userState = userStatusService.CreateOrUpdateToken(user);
            UserLoginResponseModel userLoginResponseModel = new UserLoginResponseModel(userState.getUser().getUsername(),userState.getToken());
            return userLoginResponseModel;
        }else {
            return null;
        }
    }
}
