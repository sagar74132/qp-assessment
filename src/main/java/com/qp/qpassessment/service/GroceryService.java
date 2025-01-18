package com.qp.qpassessment.service;

import com.qp.qpassessment.model.GroceryItemModel;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface GroceryService {

    List<GroceryItemModel> getAllGroceryItems();

    List<GroceryItemModel> getAvailableGroceryItems();

    GenericResponse<List<GroceryItemModel>> addGroceryItems(List<GroceryItemModel> items);

    GenericResponse<String> removeGroceryItems(List<UUID> items);

    GenericResponse<List<GroceryItemModel>> updateGroceryItems(List<GroceryItemModel> items);

    GenericResponse<String> updateInventory(Map<UUID, Integer> items);
}
