package com.shutup.service;

import com.shutup.model.persist.User;
import com.shutup.model.persist.UserStatus;
import com.shutup.repo.UserStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by shutup on 2016/12/3.
 */
@Service
public class UserStatusService {
    @Autowired
    private UserStatusRepo userStatusRepo;

    public UserStatus CreateOrUpdateToken(User user) {
        UserStatus userStatus = userStatusRepo.findByUser(user);
        String token = UUID.randomUUID().toString();
        if (userStatus != null) {
            userStatus.setToken(token);
        }else {
            userStatus = new UserStatus(token,user);
        }
        UserStatus newUserStatus = userStatusRepo.save(userStatus);
        return newUserStatus;
    }
}
