package com.qp.qpassessment.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersDetailsUpdateRequest {

    private String name;

    @Email
    private String newEmail;

    private String password;
}
