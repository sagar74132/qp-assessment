package com.qp.qpassessment.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersRequestDto {

    private String name;

    @Email
    private String email;

    private String password;
}
