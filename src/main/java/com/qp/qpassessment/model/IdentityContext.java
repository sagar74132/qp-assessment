package com.qp.qpassessment.model;

import com.qp.qpassessment.constant.Enums;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdentityContext {

    private String email;
    private Enums.Role role;
}
