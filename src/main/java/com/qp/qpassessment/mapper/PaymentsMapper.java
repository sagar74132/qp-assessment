package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.Payments;
import com.qp.qpassessment.model.PaymentsModel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class PaymentsMapper {

    public static Payments mapToPayments(PaymentsModel paymentsModel) {
        return Payments.builder()
                .id(paymentsModel.getId())
                .status(paymentsModel.getStatus())
                .orderId(paymentsModel.getOrderId())
                .build();
    }

    public static PaymentsModel mapToPaymentsModel(Payments payments) {
        return PaymentsModel.builder()
                .id(payments.getId())
                .status(payments.getStatus())
                .orderId(payments.getOrderId())
                .build();
    }
}
