package com.qp.qpassessment.service;

import com.qp.qpassessment.model.PaymentStatusUpdateRequestDto;
import com.qp.qpassessment.model.PaymentsDto;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentsService {

    GenericResponse<PaymentsDto> createPayment(PaymentsDto paymentsDto);

    GenericResponse<PaymentsDto> updatePayment(PaymentStatusUpdateRequestDto request);
}
