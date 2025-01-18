package com.qp.qpassessment.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {

    private T data;
    private String message;
    private HttpStatus status;
    private List<String> errors;
    private List<String> success;

}
