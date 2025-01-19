package com.qp.qpassessment.service;

import com.qp.qpassessment.model.OrderRequest;
import com.qp.qpassessment.model.OrderResponse;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    GenericResponse<OrderResponse> placeOrder(List<OrderRequest> orderRequests);

    GenericResponse<OrderResponse> getUserOrderList();

    GenericResponse<OrderResponse> getOrderById();
}
