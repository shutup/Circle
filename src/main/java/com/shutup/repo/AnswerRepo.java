package com.shutup.repo;

import com.shutup.model.persist.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shutup on 2017/1/28.
 */
@Repository
public interface AnswerRepo extends JpaRepository<Answer,Long> {
}
