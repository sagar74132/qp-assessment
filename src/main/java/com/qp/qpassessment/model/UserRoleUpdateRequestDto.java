package com.qp.qpassessment.model;

import com.qp.qpassessment.constant.Enums;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
@Builder
public class UserRoleUpdateRequestDto {

    private UUID id;

    @NonNull
    private Enums.Role role;
}
