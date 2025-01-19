package com.qp.qpassessment.exception;

import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

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

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<GenericResponse<String>> handleUserAlreadyExistException(UserAlreadyExistException e) {
        GenericResponse<String> response = GenericResponse.<String>builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
