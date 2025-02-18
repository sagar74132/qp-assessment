package com.qp.qpassessment.mapper;

import com.qp.qpassessment.entity.GroceryItems;
import com.qp.qpassessment.model.GroceryItemModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GroceryItemsMapper {

    public static GroceryItemModel mapToModel(GroceryItems groceryItems) {
        return GroceryItemModel.builder()
                .id(groceryItems.getId())
                .name(groceryItems.getName())
                .price(groceryItems.getPrice())
                .description(groceryItems.getDescription())
                .category(groceryItems.getCategory())
                .quantity(groceryItems.getQuantity())
                .build();
    }

    public static GroceryItems mapToEntity(GroceryItemModel groceryItemModel) {
        return GroceryItems.builder()
                .id(groceryItemModel.getId())
                .name(groceryItemModel.getName())
                .price(groceryItemModel.getPrice())
                .description(groceryItemModel.getDescription())
                .category(groceryItemModel.getCategory())
                .quantity(groceryItemModel.getQuantity())
                .build();
    }
}
