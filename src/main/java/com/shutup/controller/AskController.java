package com.shutup.controller;

import com.shutup.exception.CustomeException;
import com.shutup.model.persist.Answer;
import com.shutup.model.persist.Ask;
import com.shutup.model.persist.User;
import com.shutup.model.request.CreateAnswerRequestModel;
import com.shutup.model.request.CreateAskRequestModel;
import com.shutup.repo.AnswerRepo;
import com.shutup.repo.AskRepo;
import com.shutup.repo.UserRepo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shutup on 2017/1/28.
 */
@RestController
@RequestMapping(path = {"/ask"})
public class AskController {

    @Autowired
    AskRepo askRepo;
    @Autowired
    AnswerRepo answerRepo;
    @Autowired
    UserRepo userRepo;

    @RequestMapping(path = {"/create"},method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody CreateAskRequestModel createAskRequestModel){
        User user = userRepo.findOne(createAskRequestModel.getUserId());
        if (user != null) {
            Ask ask = askRepo.save(new Ask(createAskRequestModel.getQuestion(),user));
            if (ask != null) {
                return ResponseEntity.ok(ask);
            }else {
                throw new CustomeException("create ask failed",false, HttpStatus.FAILED_DEPENDENCY);
            }
        }else {
                throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = {"/{askId}/answer/create"},method = RequestMethod.POST)
    public ResponseEntity addAnswer(@PathVariable Long askId,@RequestBody CreateAnswerRequestModel createAnswerRequestModel){
        Ask ask = askRepo.findOne(askId);
        if (ask != null){
            User user = userRepo.findOne(createAnswerRequestModel.getUserId());
            if (user != null && ask.getUser().getId()==user.getId()) {
                Answer answer = answerRepo.save(new Answer(createAnswerRequestModel.getAnswer(),user));
                if (answer != null) {
                    ask.getAnswers().add(answer);
                    Ask ask1 = askRepo.save(ask);
                    if (ask1 != null) {
                        return ResponseEntity.ok(ask1);
                    }else {
                        throw new CustomeException("create answer failed",false, HttpStatus.FAILED_DEPENDENCY);
                    }
                }else {
                    throw new CustomeException("create answer failed",false, HttpStatus.FAILED_DEPENDENCY);
                }
            }else {
                throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
            }
        }else {
            throw new CustomeException("ask not found",false, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = {"/lists"},method = RequestMethod.GET)
    public ResponseEntity<List<Ask>> list(@RequestParam Long userId) {
        User user = userRepo.findOne(userId);
        if (user != null) {
            List<Ask> asks = askRepo.findByUser(user);
            if (asks != null) {
                return ResponseEntity.ok(asks);
            }else {
                asks = new ArrayList<>();
                return ResponseEntity.ok(asks);
            }
        }else {
            throw new CustomeException("user not found",false, HttpStatus.NOT_FOUND);
        }
    }
}
