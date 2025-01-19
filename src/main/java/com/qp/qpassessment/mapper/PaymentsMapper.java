package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.Payments;
import com.qp.qpassessment.model.PaymentsDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class PaymentsMapper {

    public static Payments mapToPayments(PaymentsDto paymentsDto) {
        return Payments.builder()
                .id(paymentsDto.getId())
                .status(paymentsDto.getStatus())
                .orderId(paymentsDto.getOrderId())
                .build();
    }

    public static PaymentsDto mapToPaymentsModel(Payments payments) {
        return PaymentsDto.builder()
                .id(payments.getId())
                .status(payments.getStatus())
                .orderId(payments.getOrderId())
                .build();
    }
}
