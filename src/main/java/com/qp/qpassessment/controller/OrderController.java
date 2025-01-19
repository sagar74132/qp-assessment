package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.OrderRequestDto;
import com.qp.qpassessment.model.OrderResponseDto;
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
    public ResponseEntity<GenericResponse<OrderResponseDto>> placeOrder(@RequestBody List<OrderRequestDto> orderRequestDto) {
        GenericResponse<OrderResponseDto> response = orderService.placeOrder(orderRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
