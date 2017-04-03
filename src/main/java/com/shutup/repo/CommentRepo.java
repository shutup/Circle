package com.shutup.repo;

import com.shutup.model.persist.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by shutup on 2017/4/2.
 */
public interface CommentRepo extends JpaRepository<Comment,Long> {
}
