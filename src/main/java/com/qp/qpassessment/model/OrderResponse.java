package com.qp.qpassessment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID orderId;
    private UUID paymentId;
    private List<OrderItemsResponse> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
}
