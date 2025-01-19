package com.qp.qpassessment.service.impl;

import com.qp.qpassessment.entity.GroceryItems;
import com.qp.qpassessment.exception.GroceryException;
import com.qp.qpassessment.mapper.GroceryItemsMapper;
import com.qp.qpassessment.model.GroceryItemModel;
import com.qp.qpassessment.repository.GroceryItemRepository;
import com.qp.qpassessment.service.GroceryService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GroceryServiceImpl implements GroceryService {

    private final GroceryItemRepository groceryItemRepository;
    private final AppConfig appConfig;

    @Autowired
    public GroceryServiceImpl(final GroceryItemRepository groceryItemRepository,
                              final AppConfig appConfig) {
        this.groceryItemRepository = groceryItemRepository;
        this.appConfig = appConfig;
    }

    @Override
    public List<GroceryItemModel> getAllGroceryItems() {

        try {
            Optional<List<GroceryItems>> groceryItems = groceryItemRepository.findAllByIsDeleted(0);

            return groceryItems.map(items -> items.stream()
                    .map(GroceryItemsMapper::mapToModel)
                    .toList()
            ).orElse(null);
        } catch (Exception e) {
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    @Override
    public List<GroceryItemModel> getAvailableGroceryItems() {

        try {
            Optional<List<GroceryItems>> groceryItems = groceryItemRepository.findAllByIsDeletedAndQuantityGreaterThan(0, 0);

            return groceryItems.map(items -> items.stream()
                    .map(GroceryItemsMapper::mapToModel)
                    .toList()
            ).orElse(null);
        } catch (Exception e) {
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    @Override
    @Transactional
    public GenericResponse<List<GroceryItemModel>> addGroceryItems(List<GroceryItemModel> groceriesList) {

        try {
            List<String> errorStruct = new ArrayList<>();

            List<GroceryItemModel> groceryItemModels = new ArrayList<>();

            groceriesList.forEach(grocery -> {
                GroceryItems groceryItem = GroceryItemsMapper.mapToEntity(grocery);

                // If an item already exists then add it in error
                groceryItemRepository.findByNameAndIsDeleted(groceryItem.getName(), 0)
                        .ifPresentOrElse(item -> {
                            errorStruct.add(appConfig.getProperty("item.already.exists", groceryItem.getName()));
                        }, () -> {
                            GroceryItems saveItem = groceryItemRepository.save(groceryItem);
                            groceryItemModels.add(GroceryItemsMapper.mapToModel(saveItem));
                        });
            });

            String message;
            HttpStatus status = null;
            if (groceryItemModels.isEmpty()) {
                message = appConfig.getProperty("items.not.added");
                status = HttpStatus.BAD_REQUEST;
            } else if (!errorStruct.isEmpty()) {
                message = appConfig.getProperty("items.added.with.errors");
            } else {
                message = appConfig.getProperty("items.added.successfully");
            }

            return GenericResponse.<List<GroceryItemModel>>builder()
                    .errors(errorStruct)
                    .success(groceryItemModels.stream().map(e -> e.getId().toString()).toList())
                    .data(groceryItemModels)
                    .message(message)
                    .status(null != status ? status : HttpStatus.CREATED)
                    .build();
        } catch (Exception e) {
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    @Override
    @Transactional
    public GenericResponse<String> removeGroceryItems(List<UUID> items) {

        try {
            List<String> errorStruct = new ArrayList<>();
            List<String> successStruct = new ArrayList<>();

            items.forEach(item -> {
                Optional<GroceryItems> groceryItem = groceryItemRepository.findById(item);

                if (groceryItem.isPresent() && groceryItem.get().getIsDeleted() == 0) {
                    groceryItem.get().setIsDeleted(1);
                    groceryItemRepository.save(groceryItem.get());
                    successStruct.add(item.toString());
                } else {
                    errorStruct.add(item.toString());
                }
            });

            String message;
            if (successStruct.isEmpty()) {
                throw new GroceryException(appConfig.getProperty("items.not.deleted"));
            } else if (!errorStruct.isEmpty()) {
                message = appConfig.getProperty("items.deleted.with.errors");
            } else {
                message = appConfig.getProperty("items.deleted.successfully");
            }

            return GenericResponse.<String>builder()
                    .errors(errorStruct)
                    .success(successStruct)
                    .message(message)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    @Override
    @Transactional
    public GenericResponse<List<GroceryItemModel>> updateGroceryItems(List<GroceryItemModel> groceriesList) {
        try {
            List<String> errorStruct = new ArrayList<>();

            List<GroceryItemModel> groceryItemModels = new ArrayList<>();

            groceriesList.forEach(grocery -> {
                Optional<GroceryItems> groceryItem = groceryItemRepository.findByIdAndIsDeleted(grocery.getId(), 0);

                if (groceryItem.isEmpty()) {
                    errorStruct.add(appConfig.getProperty("item.not.exist.for.update", grocery.getId()));
                } else if (grocery.getQuantity() < 0) {
                    errorStruct.add(appConfig.getProperty("item.quantity.invalid", grocery.getId()));
                } else {
                    GroceryItems mappedGroceryItems = mapModelToGroceryItem(grocery, groceryItem.get());
                    GroceryItems saveItem = groceryItemRepository.save(mappedGroceryItems);
                    groceryItemModels.add(GroceryItemsMapper.mapToModel(saveItem));
                }
            });

            String message;
            HttpStatus status = null;
            if (groceryItemModels.isEmpty()) {
                message = appConfig.getProperty("items.not.updated");
                status = HttpStatus.BAD_REQUEST;
            } else if (!errorStruct.isEmpty()) {
                message = appConfig.getProperty("items.updated.with.errors");
            } else {
                message = appConfig.getProperty("items.updated.successfully");
            }

            return GenericResponse.<List<GroceryItemModel>>builder()
                    .errors(errorStruct)
                    .success(groceryItemModels.stream().map(e -> e.getId().toString()).toList())
                    .data(groceryItemModels)
                    .message(message)
                    .status(null != status ? status : HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    @Override
    @Transactional
    public GenericResponse<List<GroceryItemModel>> updateInventory(Map<UUID, Integer> items) {
        try {

            List<GroceryItemModel> groceryItemModels = items.entrySet().stream()
                    .map(e -> GroceryItemModel.builder()
                            .id(e.getKey())
                            .quantity(e.getValue())
                            .build())
                    .toList();

            return updateGroceryItems(groceryItemModels);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new GroceryException(appConfig.getProperty("something.went.wrong"));
        }
    }

    public List<GroceryItems> getGroceryItemsById(List<UUID> ids) {
        return groceryItemRepository.findAllById(ids);
    }

    private GroceryItems mapModelToGroceryItem(GroceryItemModel groceryItemModel, GroceryItems item) {
        if (null != groceryItemModel.getName()
                && !groceryItemModel.getName().isEmpty()
                && !groceryItemModel.getName().equals(item.getName())) {
            item.setName(groceryItemModel.getName());
        }

        if (null != groceryItemModel.getCategory()
                && !groceryItemModel.getCategory().equals(item.getCategory())) {
            item.setCategory(groceryItemModel.getCategory());
        }

        if (null != groceryItemModel.getDescription()
                && !groceryItemModel.getDescription().equals(item.getDescription())) {
            item.setDescription(groceryItemModel.getDescription());
        }

        if (null != groceryItemModel.getPrice()
                && !groceryItemModel.getPrice().equals(item.getPrice())) {
            item.setPrice(groceryItemModel.getPrice());
        }

        if (null != groceryItemModel.getQuantity()
                && !groceryItemModel.getQuantity().equals(item.getQuantity())) {
            item.setQuantity(groceryItemModel.getQuantity());
        }

        return item;

    }
}
