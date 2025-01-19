package com.qp.qpassessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qp.qpassessment.constant.Enums;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentsModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull
    private UUID orderId;

    @NotNull
    private Enums.PaymentStatus status;
}
