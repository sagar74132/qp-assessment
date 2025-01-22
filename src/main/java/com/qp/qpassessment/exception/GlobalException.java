package com.qp.qpassessment.exception;

import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    private final AppConfig appConfig;

    @Autowired
    public GlobalException(final AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @ExceptionHandler(GroceryException.class)
    public ResponseEntity<GenericResponse<String>> handleGroceryException(GroceryException e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(OrdersException.class)
    public ResponseEntity<GenericResponse<String>> handleOrdersException(OrdersException e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(UserExceptions.class)
    public ResponseEntity<GenericResponse<String>> handleUserAlreadyExistException(UserExceptions e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(appConfig.getProperty("invalid.input.request"))
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericResponse<String>> handleBadCredentialsException(BadCredentialsException e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(appConfig.getProperty("invalid.credentials"))
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
