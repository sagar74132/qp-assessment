package com.qp.qpassessment.model;

import com.qp.qpassessment.constant.Enums;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentStatusUpdateRequest {

    @NotNull
    private UUID id;

    @NotNull
    private Enums.PaymentStatus status;
}
