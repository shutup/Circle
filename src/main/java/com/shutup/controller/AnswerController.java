package com.shutup.controller;

import com.shutup.common.Constants;
import com.shutup.model.persist.Question;
import com.shutup.repo.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shutup on 2017/4/3.
 */
@RestController
@RequestMapping(path = {"/answer"})
public class AnswerController implements Constants{

    @Autowired
    QuestionRepo questionRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    CommentRepo commentRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserStatusRepo userStatusRepo;

//    @RequestMapping(path = {"/{questionId}/answer/{answerId}/agree"},method = RequestMethod.GET)
//    public ResponseEntity<Question> agree(@NotEmpty @RequestHeader String token, @RequestParam Long questionId) {
//        Question question = processAgreeAndDisagree(token,questionId,STATE_AGREE);
//        return ResponseEntity.ok(question);
//    }
//
//    @RequestMapping(path = {"/{questionId}/answer/{answerId}/disagree"},method = RequestMethod.GET)
//    public ResponseEntity<Question> disagree(@NotEmpty @RequestHeader String token, @RequestParam Long questionId) {
//        Question question = processAgreeAndDisagree(token,questionId,STATE_DISAGREE);
//        return ResponseEntity.ok(question);
//    }
}
