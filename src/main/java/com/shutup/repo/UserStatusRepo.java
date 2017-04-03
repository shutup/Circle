package com.shutup.repo;

import com.shutup.model.persist.User;
import com.shutup.model.persist.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by shutup on 2017/4/2.
 */
public interface UserStatusRepo extends JpaRepository<UserStatus,Long> {
    UserStatus findByToken(String token);

    UserStatus findByUser(User user);
}
