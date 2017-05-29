package com.shutup.controller;

import com.shutup.common.Constants;
import com.shutup.exception.CustomeException;
import com.shutup.model.persist.Answer;
import com.shutup.model.persist.Comment;
import com.shutup.model.persist.Question;
import com.shutup.model.persist.UserStatus;
import com.shutup.model.request.CreateQuestionRequestModel;
import com.shutup.model.request.QuestionAddAnswerRequestModel;
import com.shutup.model.request.QuestionAddCommentRequestModel;
import com.shutup.repo.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shutup on 2017/1/28.
 */
@RestController
@RequestMapping(path = {"/v1"})
public class QuestionController implements Constants {

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

    @RequestMapping(path = {"/questions"}, method = RequestMethod.POST)
    public ResponseEntity addQuestion(@RequestBody CreateQuestionRequestModel createAskRequestModel, @NotEmpty @RequestHeader String token) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found", false, HttpStatus.NOT_FOUND);
        }
        Question question = questionRepo.save(new Question(createAskRequestModel.getQuestion(), userStatus.getUser()));
        if (question != null) {
            return ResponseEntity.ok(question);
        } else {
            throw new CustomeException("create question failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
    }


//    @RequestMapping(path = {"/questions"},method = RequestMethod.GET)
//    public ResponseEntity<List<Question>> list(@RequestParam Long userId, @NotEmpty @RequestHeader String token) {
//        UserStatus userStatus = userStatusRepo.findByToken(token);
//        if (userStatus!=null) {
//            User user = userRepo.findOne(userId);
//            if (user != null) {
//                List<Question> questions = questionRepo.findByUser(user);
//                if (questions != null) {
//                    return ResponseEntity.ok(questions);
//                }else {
//                    questions = new ArrayList<>();
//                    return ResponseEntity.ok(questions);
//                }
//            }else {
//                throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
//            }
//        }else {
//            throw new CustomeException("user not found",false, HttpStatus.UNAUTHORIZED);
//        }
//    }

    @RequestMapping(path = {"/questions"}, method = RequestMethod.GET)
    public ResponseEntity<Page<Question>> questions(@NotEmpty @RequestHeader String token,
                                                    Pageable pageable) {
        UserStatus userStatus = getUserStatus(token);
        Page<Question> result;
        if (pageable.getSort() != null) {
            if (pageable.getSort().getOrderFor("agreeUsersCount") !=null ) {
                result = questionRepo.findByAgreedUsers(getPageable(pageable));
            } else if (pageable.getSort().getOrderFor("disagreeUsersCount") != null){
                result = questionRepo.findByDisagreedUsers(getPageable(pageable));
            }else {
                result =  questionRepo.findAll(getPageable(pageable));
            }
        }else {
            result =  questionRepo.findAll(getPageable(pageable));
        }
        return ResponseEntity.ok(result);
    }

    private PageRequest getPageable(Pageable pageable) {
        return new PageRequest(pageable.getPageNumber(),
                pageable.getPageSize(),
                new Sort(Sort.Direction.DESC,"createdAt"));
    }

    @RequestMapping(path = {"/questions/{questionId}/agree"}, method = RequestMethod.PUT)
    public ResponseEntity<Question> agreeQuestion(@NotEmpty @RequestHeader String token,
                                                  @PathVariable Long questionId) {
        Question question = processAgreeAndDisagreeQuestion(token, questionId, STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/questions/{questionId}/agree"}, method = RequestMethod.DELETE)
    public ResponseEntity<Question> disagreeQuestion(@NotEmpty @RequestHeader String token,
                                                     @PathVariable Long questionId) {
        Question question = processAgreeAndDisagreeQuestion(token, questionId, STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }


    @RequestMapping(path = {"/questions/{questionId}/answers"}, method = RequestMethod.POST)
    public ResponseEntity addAnswer(@PathVariable Long questionId, @RequestBody QuestionAddAnswerRequestModel questionAddAnswerRequestModel, @NotEmpty @RequestHeader String token) {
        Question question = getQuestion(questionId);
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null && question.getUser().getId() != userStatus.getUser().getId()) {
            throw new CustomeException("user not found", false, HttpStatus.NOT_FOUND);
        }
        Answer answer = answerRepo.save(new Answer(questionAddAnswerRequestModel.getAnswer(), userStatus.getUser()));
        if (answer == null) {
            throw new CustomeException("create answer failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
        question.getAnswers().add(answer);
        Question question1 = questionRepo.save(question);
        if (question1 != null) {
            return ResponseEntity.ok(question1);
        } else {
            throw new CustomeException("create answer failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/agree"}, method = RequestMethod.PUT)
    public ResponseEntity<Question> agreeAnswer(@NotEmpty @RequestHeader String token,
                                                @PathVariable Long questionId,
                                                @PathVariable Long answerId) {
        Question question = processAgreeAndDisagreeAnswer(token, questionId, answerId, STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/agree"}, method = RequestMethod.DELETE)
    public ResponseEntity<Question> disagreeAnswer(@NotEmpty @RequestHeader String token,
                                                   @PathVariable Long questionId,
                                                   @PathVariable Long answerId) {
        Question question = processAgreeAndDisagreeAnswer(token, questionId, answerId, STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/comments"}, method = RequestMethod.POST)
    public ResponseEntity<Question> addComment(@NotEmpty @RequestHeader String token,
                                               @RequestBody QuestionAddCommentRequestModel questionAddCommentRequestModel,
                                               @PathVariable Long questionId,
                                               @PathVariable Long answerId) {
        UserStatus userStatus = getUserStatus(token);

        Question question = getQuestion(questionId);

        Answer answer = getAnswer(answerId);

        if (!question.getAnswers().contains(answer)) {
            throw new CustomeException("question and answer not match", false, HttpStatus.FAILED_DEPENDENCY);
        }

        Comment comment = questionAddCommentRequestModel.createComment(userStatus.getUser());
        Comment newComment = commentRepo.save(comment);
        if (newComment == null) {
            throw new CustomeException("create comment failed", false, HttpStatus.FAILED_DEPENDENCY);
        }

        answer.getComments().add(newComment);
        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            return ResponseEntity.ok(question);
        } else {
            throw new CustomeException("add comment failed", false, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/comments/{commentId}/agree"}, method = RequestMethod.PUT)
    public ResponseEntity<Question> agreeComment(@NotEmpty @RequestHeader String token,
                                                 @PathVariable Long questionId,
                                                 @PathVariable Long answerId,
                                                 @PathVariable Long commentId
    ) {
        Question question = processAgreeAndDisagreeComment(token, questionId, answerId, commentId, STATE_AGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/comments/{commentId}/agree"}, method = RequestMethod.DELETE)
    public ResponseEntity<Question> disagreeComment(@NotEmpty @RequestHeader String token,
                                                    @PathVariable Long questionId,
                                                    @PathVariable Long answerId,
                                                    @PathVariable Long commentId
    ) {
        Question question = processAgreeAndDisagreeComment(token, questionId, answerId, commentId, STATE_DISAGREE);
        return ResponseEntity.ok(question);
    }

    @RequestMapping(path = {"/questions/{questionId}/answers/{answerId}/comments/{commentId}/replies"}, method = RequestMethod.POST)
    public ResponseEntity<Question> replyComment(@NotEmpty @RequestHeader String token,
                                                 @RequestBody QuestionAddCommentRequestModel questionAddCommentRequestModel,
                                                 @PathVariable Long questionId,
                                                 @PathVariable Long answerId,
                                                 @PathVariable Long commentId
    ) {
        UserStatus userStatus = getUserStatus(token);

        Question question = getQuestion(questionId);

        Answer answer = getAnswer(answerId);

        if (!question.getAnswers().contains(answer)) {
            throw new CustomeException("question and answer not match", false, HttpStatus.FAILED_DEPENDENCY);
        }

        Comment comment = getComment(commentId);
        Comment replyComment = questionAddCommentRequestModel.createReplyComment(comment.getUser(), userStatus.getUser());
        Comment newReplyComment = commentRepo.save(replyComment);
        if (newReplyComment == null) {
            throw new CustomeException("create reply comment failed", false, HttpStatus.FAILED_DEPENDENCY);
        }

        answer.getComments().add(replyComment);
        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            return ResponseEntity.ok(question);
        } else {
            throw new CustomeException("add comment failed", false, HttpStatus.NOT_FOUND);
        }
    }

    private Question processAgreeAndDisagreeQuestion(String token, Long questionId, int state) {
        UserStatus userStatus = getUserStatus(token);
        Question question = getQuestion(questionId);
        if (state == STATE_AGREE) {
            processAgreeQuestion(userStatus, question);
        } else if (state == STATE_DISAGREE) {
            processDisagreeQuestion(userStatus, question);
        }

        Question newQuestion = questionRepo.save(question);
        if (newQuestion != null) {
            return newQuestion;
        } else {
            throw new CustomeException("agree or disagree question failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private void processDisagreeQuestion(UserStatus userStatus, Question question) {
        if (question.getDisagreedUsers().contains(userStatus.getUser())) {
            question.getDisagreedUsers().remove(userStatus.getUser());
        } else {
            question.getDisagreedUsers().add(userStatus.getUser());
            question.getAgreedUsers().remove(userStatus.getUser());
        }
    }

    private void processAgreeQuestion(UserStatus userStatus, Question question) {
        if (question.getAgreedUsers().contains(userStatus.getUser())) {
            question.getAgreedUsers().remove(userStatus.getUser());
        } else {
            question.getAgreedUsers().add(userStatus.getUser());
            question.getDisagreedUsers().remove(userStatus.getUser());
        }
    }

    private Question processAgreeAndDisagreeAnswer(String token, Long questionId, Long answerId, int state) {
        UserStatus userStatus = getUserStatus(token);
        checkQuestionExist(questionId);
        Answer answer = getAnswer(answerId);

        if (state == STATE_AGREE) {
            if (answer.getAgreedUsers().contains(userStatus.getUser())) {
                answer.getAgreedUsers().remove(userStatus.getUser());
            } else {
                answer.getAgreedUsers().add(userStatus.getUser());
                answer.getDisagreedUsers().remove(userStatus.getUser());
            }
        } else if (state == STATE_DISAGREE) {
            if (answer.getDisagreedUsers().contains(userStatus.getUser())) {
                answer.getDisagreedUsers().remove(userStatus.getUser());
            } else {
                answer.getAgreedUsers().remove(userStatus.getUser());
                answer.getDisagreedUsers().add(userStatus.getUser());
            }
        }

        Answer newAnswer = answerRepo.save(answer);
        if (newAnswer != null) {
            Question newQuestion = questionRepo.findOne(questionId);
            return newQuestion;
        } else {
            throw new CustomeException("agree or disagree answer failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private Question processAgreeAndDisagreeComment(String token, Long questionId, Long answerId, Long commentId, int state) {
        UserStatus userStatus = getUserStatus(token);
        checkQuestionExist(questionId);
        checkAnswerExist(answerId);
        Comment comment = getComment(commentId);

        if (state == STATE_AGREE) {
            if (comment.getAgreedUsers().contains(userStatus.getUser())) {
                comment.getAgreedUsers().remove(userStatus.getUser());
            } else {
                comment.getAgreedUsers().add(userStatus.getUser());
                comment.getDisagreedUsers().remove(userStatus.getUser());
            }
        } else if (state == STATE_DISAGREE) {
            if (comment.getDisagreedUsers().contains(userStatus.getUser())) {
                comment.getDisagreedUsers().remove(userStatus.getUser());
            } else {
                comment.getDisagreedUsers().add(userStatus.getUser());
                comment.getAgreedUsers().remove(userStatus.getUser());
            }
        }

        Comment newComment = commentRepo.save(comment);
        if (newComment != null) {
            Question newQuestion = questionRepo.findOne(questionId);
            return newQuestion;
        } else {
            throw new CustomeException("agree or disagree comment failed", false, HttpStatus.FAILED_DEPENDENCY);
        }
    }

    private UserStatus getUserStatus(String token) {
        UserStatus userStatus = userStatusRepo.findByToken(token);
        if (userStatus == null) {
            throw new CustomeException("user not found", false, HttpStatus.UNAUTHORIZED);
        }
        return userStatus;
    }

    private Question getQuestion(Long questionId) {
        Question question = questionRepo.findOne(questionId);
        if (question == null) {
            throw new CustomeException("question not found", false, HttpStatus.NOT_FOUND);
        }
        return question;
    }

    private Answer getAnswer(Long answerId) {
        Answer answer = answerRepo.findOne(answerId);
        if (answer == null) {
            throw new CustomeException("answer not found", false, HttpStatus.NOT_FOUND);
        }
        return answer;
    }

    private Comment getComment(Long commentId) {
        Comment comment = commentRepo.findOne(commentId);
        if (comment == null) {
            throw new CustomeException("comment not found", false, HttpStatus.NOT_FOUND);
        }
        return comment;
    }

    private void checkQuestionExist(Long questionId) {
        Question question = getQuestion(questionId);
    }

    private void checkAnswerExist(Long answerId) {
        Answer answer = getAnswer(answerId);
    }

}
