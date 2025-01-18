package com.qp.qpassessment.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
