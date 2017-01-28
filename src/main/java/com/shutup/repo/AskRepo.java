package com.shutup.repo;

import com.shutup.model.persist.Ask;
import com.shutup.model.persist.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shutup on 2017/1/28.
 */
@Repository
public interface AskRepo extends JpaRepository<Ask,Long> {
    List<Ask> findByUser(User user);
}
