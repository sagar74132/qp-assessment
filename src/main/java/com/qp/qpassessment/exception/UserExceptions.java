package com.qp.qpassessment.exception;

public class UserExceptions extends RuntimeException {
    public UserExceptions(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
