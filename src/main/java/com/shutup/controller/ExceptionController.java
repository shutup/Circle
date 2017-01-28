package com.shutup.controller;

import com.shutup.exception.CustomeException;
import com.shutup.model.response.RestMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shutup on 2017/1/28.
 */
@ControllerAdvice
@RestController
public class ExceptionController {

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<RestMessage> handleDataIntegrityViolationException(DataIntegrityViolationException d){
        return new ResponseEntity(new RestMessage("database operation error",false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = CustomeException.class)
    public ResponseEntity<RestMessage> handleCustomeException(CustomeException c) {
        return new ResponseEntity<RestMessage>(new RestMessage(c.getMessage(),c.isSuccess()),c.getHttpStatus());
    }
}
