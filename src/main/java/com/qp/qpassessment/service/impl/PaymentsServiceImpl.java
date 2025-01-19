package com.qp.qpassessment.service.impl;

import com.qp.qpassessment.entity.Payments;
import com.qp.qpassessment.exception.PaymentException;
import com.qp.qpassessment.mapper.PaymentsMapper;
import com.qp.qpassessment.model.PaymentStatusUpdateRequest;
import com.qp.qpassessment.model.PaymentsModel;
import com.qp.qpassessment.repository.PaymentRepository;
import com.qp.qpassessment.service.PaymentsService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentRepository paymentRepository;
    private final AppConfig appConfig;

    public PaymentsServiceImpl(final PaymentRepository paymentRepository,
                               final AppConfig appConfig) {
        this.paymentRepository = paymentRepository;
        this.appConfig = appConfig;
    }

    @Override
    @Transactional
    public GenericResponse<PaymentsModel> createPayment(PaymentsModel paymentsModel) {
        Payments payment = PaymentsMapper.mapToPayments(paymentsModel);

        payment = paymentRepository.save(payment);

        paymentsModel.setId(payment.getId());

        return GenericResponse.<PaymentsModel>builder()
                .data(paymentsModel)
                .message(appConfig.getProperty("payment.created", payment.getId()))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    @Transactional
    public GenericResponse<PaymentsModel> updatePayment(PaymentStatusUpdateRequest request) {

        Optional<Payments> payment = paymentRepository.findById(request.getId());

        if (payment.isEmpty()) {
            throw new PaymentException(appConfig.getProperty("payment.not.found", request.getId()));
        }

        Payments updatePayment = paymentRepository.save(payment.get());

        return GenericResponse.<PaymentsModel>builder()
                .data(PaymentsModel.builder()
                        .id(updatePayment.getId())
                        .orderId(updatePayment.getOrderId())
                        .status(updatePayment.getStatus())
                        .build())
                .message(appConfig.getProperty("payment.status.updated", updatePayment.getId()))
                .status(HttpStatus.CREATED)
                .build();
    }
}
