package com.shutup.controller;

import com.shutup.common.Constants;
import com.shutup.exception.CustomeException;
import com.shutup.model.persist.*;
import com.shutup.model.request.QuestionAddAnswerRequestModel;
import com.shutup.model.request.CreateQuestionRequestModel;
import com.shutup.model.request.QuestionAddCommentRequestModel;
import com.shutup.repo.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shutup on 2017/1/28.
 */
@RestController
@RequestMapping(path = {"/question"})
public class QuestionController implements Constants{

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

    @RequestMapping(path = {"/create"},method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CreateQuestionRequestModel createAskRequestModel, @NotEmpty @RequestHeader String token){
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
        }
        Question question = questionRepo.save(new Question(createAskRequestModel.getQuestion(),userStatus.getUser()));
        if (question != null) {
            return ResponseEntity.ok(question);
        }else {
            throw new CustomeException("create question failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @RequestMapping(path = {"/{questionId}/answer/create"},method = RequestMethod.POST)
    public ResponseEntity addAnswer(@PathVariable Long questionId, @RequestBody QuestionAddAnswerRequestModel questionAddAnswerRequestModel, @NotEmpty @RequestHeader String token){
        Question question = questionRepo.findOne(questionId);
        if (question == null){
            throw new CustomeException("question not found",false, HttpStatus.NOT_FOUND);
        }
        UserStatus  userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null && question.getUser().getId() != userStatus.getUser().getId()) {
            throw new CustomeException("user not found", false, HttpStatus.NOT_FOUND);
        }
        Answer answer = answerRepo.save(new Answer(questionAddAnswerRequestModel.getAnswer(),userStatus.getUser()));
        if (answer == null) {
            throw new CustomeException("create answer failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
        question.getAnswers().add(answer);
        Question question1 = questionRepo.save(question);
        if (question1 != null) {
            return ResponseEntity.ok(question1);
        }else {
            throw new CustomeException("create answer failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @RequestMapping(path = {"/lists/user/"},method = RequestMethod.GET)
    public ResponseEntity<List<Question>> list(@RequestParam Long userId, @NotEmpty @RequestHeader String token) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus!=null) {
            User user = userRepo.findOne(userId);
            if (user != null) {
                List<Question> questions = questionRepo.findByUser(user);
                if (questions != null) {
                    return ResponseEntity.ok(questions);
                }else {
                    questions = new ArrayList<>();
                    return ResponseEntity.ok(questions);
                }
            }else {
                throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
            }
        }else {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = {"/lists"},method = RequestMethod.GET)
    public ResponseEntity<Page<Question>> getTotalList(@NotEmpty @RequestHeader String token, @RequestParam(defaultValue = "0") int page) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }

        Page<Question> result =  questionRepo.findAll(new PageRequest(page,10,new Sort(Sort.Direction.DESC,"createdAt")));
        return ResponseEntity.ok(result);
    }

    @RequestMapping(path = {"/{questionId}/agree"},method = RequestMethod.GET)
    public ResponseEntity<Question> agreeQuestion(@NotEmpty @RequestHeader String token,
                                                  @PathVariable Long questionId) {
        Question question = processAgreeAndDisagreeQuestion(token,questionId,STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/disagree"},method = RequestMethod.GET)
    public ResponseEntity<Question> disagreeQuestion(@NotEmpty @RequestHeader String token,
                                                     @PathVariable Long questionId) {
        Question question = processAgreeAndDisagreeQuestion(token,questionId,STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/comment"},method = RequestMethod.POST)
    public ResponseEntity<Question> addComment(@NotEmpty @RequestHeader String token,
                           @RequestBody QuestionAddCommentRequestModel questionAddCommentRequestModel,
                           @PathVariable Long questionId,
                           @PathVariable Long answerId) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }

        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found",false,HttpStatus.NOT_FOUND);
        }

        Answer answer = answerRepo.findOne(answerId);
        if (answer == null) {
            throw new CustomeException("answer not found",false,HttpStatus.NOT_FOUND);
        }

        if (!question.getAnswers().contains(answer)) {
            throw new CustomeException("question and answer not match",false,HttpStatus.FAILED_DEPENDENCY);
        }

        Comment comment = questionAddCommentRequestModel.createComment(userStatus.getUser());
        Comment newComment = commentRepo.save(comment);
        if (newComment == null) {
            throw new CustomeException("create comment failed",false, HttpStatus.FAILED_DEPENDENCY);
        }

        answer.getComments().add(newComment);
        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            return ResponseEntity.ok(question);
        }else {
            throw new CustomeException("add comment failed",false,HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/agree"},method = RequestMethod.GET)
    public ResponseEntity<Question> agreeAnswer(@NotEmpty @RequestHeader String token,
                                                @PathVariable Long questionId,
                                                @PathVariable Long answerId) {
        Question question = processAgreeAndDisagreeAnswer(token,questionId,answerId,STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/disagree"},method = RequestMethod.GET)
    public ResponseEntity<Question> disagreeAnswer(@NotEmpty @RequestHeader String token,
                                                   @PathVariable Long questionId,
                                                   @PathVariable Long answerId) {
        Question question = processAgreeAndDisagreeAnswer(token,questionId,answerId,STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/comment/{commentId}/agree"},method = RequestMethod.GET)
    public ResponseEntity<Question> agreeComment(@NotEmpty @RequestHeader String token,
                                                @PathVariable Long questionId,
                                                @PathVariable Long answerId,
                                                @PathVariable Long commentId
    ) {
        Question question = processAgreeAndDisagreeComment(token,questionId,answerId,commentId,STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/comment/{commentId}/disagree"},method = RequestMethod.GET)
    public ResponseEntity<Question> disagreeComment(@NotEmpty @RequestHeader String token,
                                                   @PathVariable Long questionId,
                                                   @PathVariable Long answerId,
                                                   @PathVariable Long commentId
    ) {
        Question question = processAgreeAndDisagreeComment(token,questionId,answerId,commentId,STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/{questionId}/answer/{answerId}/comment/{commentId}/reply"},method = RequestMethod.POST)
    public ResponseEntity<Question> replyComment(@NotEmpty @RequestHeader String token,
                                                 @RequestBody QuestionAddCommentRequestModel questionAddCommentRequestModel,
                                                 @PathVariable Long questionId,
                                                 @PathVariable Long answerId,
                                                 @PathVariable Long commentId
    ) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }

        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found",false,HttpStatus.NOT_FOUND);
        }

        Answer answer = answerRepo.findOne(answerId);
        if (answer == null) {
            throw new CustomeException("answer not found",false,HttpStatus.NOT_FOUND);
        }

        if (!question.getAnswers().contains(answer)) {
            throw new CustomeException("question and answer not match",false,HttpStatus.FAILED_DEPENDENCY);
        }

        Comment comment = commentRepo.findOne(commentId);
        if (comment == null) {
            throw new CustomeException("",false, HttpStatus.NOT_FOUND);
        }

        Comment replyComment = questionAddCommentRequestModel.createReplyComment(comment.getUser(),userStatus.getUser());
        Comment newReplyComment = commentRepo.save(replyComment);
        if (newReplyComment == null) {
            throw new CustomeException("create reply comment failed",false, HttpStatus.FAILED_DEPENDENCY);
        }

        answer.getComments().add(replyComment);
        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            return ResponseEntity.ok(question);
        }else {
            throw new CustomeException("add comment failed",false,HttpStatus.NOT_FOUND);
        }
    }



    private Question processAgreeAndDisagreeQuestion(String token, Long questionId, int state) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }
        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found",false,HttpStatus.NOT_FOUND);
        }
        if (state == STATE_AGREE) {
            question.getAgreedUsers().add(userStatus.getUser());
        }else if (state == STATE_DISAGREE) {
            question.getDisagreedUsers().add(userStatus.getUser());
        }
        Question newQuestion = questionRepo.save(question);
        if (newQuestion != null) {
            return newQuestion;
        }else {
            throw new CustomeException("agree or disagree question failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private Question processAgreeAndDisagreeAnswer(String token, Long questionId, Long answerId,int state) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }
        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found",false,HttpStatus.NOT_FOUND);
        }
        Answer answer = answerRepo.findOne(answerId);
        if (answer == null) {
            throw new CustomeException("",false,HttpStatus.NOT_FOUND);
        }

        if (state == STATE_AGREE) {
            answer.getAgreedUsers().add(userStatus.getUser());
        }else if (state == STATE_DISAGREE) {
            answer.getDisagreedUsers().add(userStatus.getUser());
        }

        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            Question newQuestion = questionRepo.findOne(questionId);
            return newQuestion;
        }else {
            throw new CustomeException("agree or disagree answer failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private Question processAgreeAndDisagreeComment(String token, Long questionId, Long answerId,Long commentId,int state) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
        }
        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found",false,HttpStatus.NOT_FOUND);
        }
        Answer answer = answerRepo.findOne(answerId);
        if (answer == null) {
            throw new CustomeException("answer not found",false,HttpStatus.NOT_FOUND);
        }
        Comment comment = commentRepo.findOne(commentId);
        if (comment == null) {
            throw new CustomeException("comment not found", false, HttpStatus.NOT_FOUND);
        }

        if (state == STATE_AGREE) {
            comment.getAgreedUsers().add(userStatus.getUser());
        }else if (state == STATE_DISAGREE) {
            comment.getDisagreedUsers().add(userStatus.getUser());
        }

        Comment newComment = commentRepo.save(comment);
        if (newComment != null) {
            Question newQuestion = questionRepo.findOne(questionId);
            return newQuestion;
        }else {
            throw new CustomeException("agree or disagree comment failed",false, HttpStatus.FAILED_DEPENDENCY);
        }
    }


}
