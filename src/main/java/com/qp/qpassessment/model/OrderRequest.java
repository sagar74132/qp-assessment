package com.qp.qpassessment.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderRequest {

    @NotNull(message = "${order.grocery.id.not.provided}")
    private UUID groceryItemId;

    @NotNull(message = "${order.grocery.item.quantity.invalid}")
    @Min(value = 1, message = "${order.grocery.item.quantity.invalid}")
    private Integer quantity;
}
