package com.qp.qpassessment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
