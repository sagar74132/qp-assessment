package com.qp.qpassessment.service;

import com.qp.qpassessment.model.PaymentStatusUpdateRequest;
import com.qp.qpassessment.model.PaymentsModel;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

@Service
public interface PaymentsService {

    GenericResponse<PaymentsModel> createPayment(PaymentsModel paymentsModel);

    GenericResponse<PaymentsModel> updatePayment(PaymentStatusUpdateRequest request);
}
