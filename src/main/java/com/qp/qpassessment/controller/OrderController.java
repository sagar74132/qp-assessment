package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.OrderRequest;
import com.qp.qpassessment.model.OrderResponse;
import com.qp.qpassessment.service.OrderService;
import com.qp.qpassessment.service.impl.OrderServiceImpl;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final AppConfig appConfig;

    @Autowired
    public OrderController(OrderServiceImpl orderService, AppConfig appConfig) {
        this.orderService = orderService;
        this.appConfig = appConfig;
    }

    @PostMapping("")
    public ResponseEntity<GenericResponse<OrderResponse>> placeOrder(@RequestBody List<OrderRequest> orderRequest) {
        GenericResponse<OrderResponse> response = orderService.placeOrder(orderRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
