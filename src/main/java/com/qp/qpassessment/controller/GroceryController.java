package com.qp.qpassessment.controller;

import com.qp.qpassessment.model.GroceryItemModel;
import com.qp.qpassessment.service.GroceryService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/grocery")
public class GroceryController {

    private final GroceryService groceryService;
    private final AppConfig appConfig;

    @Autowired
    private GroceryController(final GroceryService groceryService,
                              final AppConfig appConfig) {
        this.groceryService = groceryService;
        this.appConfig = appConfig;
    }

    @GetMapping("/all")
    public ResponseEntity<GenericResponse<List<GroceryItemModel>>> getAllGroceryItems() {
        List<GroceryItemModel> groceryItemModels = groceryService.getAllGroceryItems();
        GenericResponse<List<GroceryItemModel>> response = GenericResponse.<List<GroceryItemModel>>builder()
                .data(groceryItemModels)
                .message(appConfig.getProperty("items.fetched.successfully"))
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/available")
    public ResponseEntity<GenericResponse<List<GroceryItemModel>>> getAllAvailableGroceryItems() {
        List<GroceryItemModel> groceryItemModels = groceryService.getAvailableGroceryItems();
        GenericResponse<List<GroceryItemModel>> response = GenericResponse.<List<GroceryItemModel>>builder()
                .data(groceryItemModels)
                .message(appConfig.getProperty("items.fetched.successfully"))
                .status(HttpStatus.OK)
                .build();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<GenericResponse<List<GroceryItemModel>>> addGroceryItems(@RequestBody List<GroceryItemModel> items) {
        GenericResponse<List<GroceryItemModel>> response = groceryService.addGroceryItems(items);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse<List<GroceryItemModel>>> updateGroceryItems(@RequestBody List<GroceryItemModel> items) {
        GenericResponse<List<GroceryItemModel>> response = groceryService.updateGroceryItems(items);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/inventory")
    public ResponseEntity<GenericResponse<String>> inventoryUpdate(@RequestBody Map<UUID, Integer> items) {
        GenericResponse<String> response = groceryService.updateInventory(items);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<GenericResponse<String>> removeGroceryItems(@RequestBody List<UUID> items) {
        GenericResponse<String> response = groceryService.removeGroceryItems(items);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
