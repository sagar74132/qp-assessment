package com.qp.qpassessment.model;

import com.qp.qpassessment.constant.Enums;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class GroceryItemModel {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
    private Enums.Category category;
    private Integer quantity;
}
