package com.qp.qpassessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qp.qpassessment.constant.Enums;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UsersResponseDto {

    private UUID id;

    private String name;

    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Enums.Role role;
}
