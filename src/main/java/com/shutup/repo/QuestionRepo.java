package com.shutup.repo;

import com.shutup.model.persist.Question;
import com.shutup.model.persist.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shutup on 2017/1/28.
 */
@Repository
public interface QuestionRepo extends JpaRepository<Question,Long> {
    List<Question> findByUser(User user);
}
