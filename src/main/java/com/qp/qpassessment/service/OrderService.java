package com.qp.qpassessment.service;

import com.qp.qpassessment.model.OrderRequestDto;
import com.qp.qpassessment.model.OrderResponseDto;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    GenericResponse<OrderResponseDto> placeOrder(List<OrderRequestDto> orderRequestDtos);

    GenericResponse<OrderResponseDto> getUserOrderList();

    GenericResponse<OrderResponseDto> getOrderById();
}
