package com.qp.qpassessment.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemsResponseDto {

    private UUID groceryItemId;
    private String groceryName;
    private Integer quantity;
    private BigDecimal price;
}
