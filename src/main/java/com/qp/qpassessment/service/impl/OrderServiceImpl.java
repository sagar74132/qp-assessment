package com.qp.qpassessment.service.impl;


import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.entity.GroceryItems;
import com.qp.qpassessment.entity.OrderItems;
import com.qp.qpassessment.entity.Orders;
import com.qp.qpassessment.entity.Payments;
import com.qp.qpassessment.exception.OrdersException;
import com.qp.qpassessment.mapper.PaymentsMapper;
import com.qp.qpassessment.model.OrderItemsResponse;
import com.qp.qpassessment.model.OrderRequest;
import com.qp.qpassessment.model.OrderResponse;
import com.qp.qpassessment.model.PaymentsModel;
import com.qp.qpassessment.repository.OrderItemsRepository;
import com.qp.qpassessment.repository.OrdersRepository;
import com.qp.qpassessment.service.GroceryService;
import com.qp.qpassessment.service.OrderService;
import com.qp.qpassessment.service.PaymentsService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final PaymentsServiceImpl paymentsService;
    private final GroceryServiceImpl groceryService;
    private final AppConfig appConfig;

    @Autowired
    public OrderServiceImpl(final OrdersRepository ordersRepository,
                            final OrderItemsRepository orderItemsRepository,
                            final PaymentsServiceImpl paymentsService,
                            final GroceryServiceImpl groceryService,
                            final AppConfig appConfig) {
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.paymentsService = paymentsService;
        this.groceryService = groceryService;
        this.appConfig = appConfig;
    }

    @Override
    @Transactional
    public GenericResponse<OrderResponse> placeOrder(List<OrderRequest> orderRequests) {

        List<GroceryItems> groceryItems = groceryService.getGroceryItemsById(orderRequests.stream()
                .map(OrderRequest::getGroceryItemId)
                .toList()
        );

        if (groceryItems.isEmpty() || groceryItems.size() != orderRequests.size()) {
            throw new OrdersException(appConfig.getProperty("order.invalid.items"));
        }

        // Throw error if the requested quantity is greater than the available quantity
        Map<UUID, Integer> invalidGroceryItem = validateGroceryQuantity(orderRequests, groceryItems);
        if (null != invalidGroceryItem) {
            GroceryItems item = groceryItems.stream()
                    .filter(e -> e.getId().equals(invalidGroceryItem.keySet().toArray()[0]))
                    .findFirst().orElse(GroceryItems.builder().build());

            throw new OrdersException(appConfig.getProperty("order.grocery.item.quantity.exhaust",
                    item.getName(),
                    item.getQuantity()
            ));
        }

        Orders order = Orders.builder()
                .userId(UUID.fromString("80cb39fe-374e-4d23-b6ca-87d294d32514")) // TODO: Change it once Spring security is in place
                .totalPrice(BigDecimal.ZERO)
                .status(Enums.OrderItemStatus.PENDING)
                .build();

        // Create Order.
        // Will create orderId using it we will save orderItems.
        Orders createdOrder = ordersRepository.save(order);

        // Create a pending payment
        Payments payment = createDefaultPendingPayment(createdOrder);

        // Create orderItems and update inventory
        List<OrderItems> orderItems = processOrderItems(orderRequests, groceryItems, createdOrder);


        // Calculate total price of the order
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItems::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Update Order's totalPrice
        createdOrder.setTotalPrice(totalPrice);
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder = ordersRepository.save(order);

        // Create OrderItems
        orderItemsRepository.saveAll(orderItems);

        List<OrderItemsResponse> orderItemsResponses = orderItems.stream()
                .map(e -> OrderItemsResponse.builder()
                        .groceryItemId(e.getGroceryItemId())
                        .groceryName(getGroceryNameFromGroceryId(groceryItems, e.getGroceryItemId()))
                        .quantity(e.getQuantity())
                        .price(e.getPrice())
                        .build())
                .toList();

        OrderResponse response = OrderResponse.builder()
                .orderId(createdOrder.getId())
                .orderItems(orderItemsResponses)
                .createdAt(createdOrder.getCreatedAt())
                .totalPrice(createdOrder.getTotalPrice())
                .paymentId(payment.getId())
                .build();

        return GenericResponse.<OrderResponse>builder()
                .data(response)
                .message(appConfig.getProperty("order.place.success", order.getId()))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public GenericResponse<OrderResponse> getUserOrderList() {
        return null;
    }

    @Override
    public GenericResponse<OrderResponse> getOrderById() {
        return null;
    }

    /**
     * This method will validate the grocery quantity
     *
     * @param orderRequests user requested grocery items
     * @param groceryItems  groceryItems from inventory
     * @return Map<UUID, Integer> if found any quantity invalid for a grocery otherwise null.
     */
    private Map<UUID, Integer> validateGroceryQuantity(List<OrderRequest> orderRequests, List<GroceryItems> groceryItems) {
        Map<UUID, Integer> groceryItemQuantityMap = groceryItems.stream()
                .collect(HashMap::new, (map, item) -> map.put(item.getId(), item.getQuantity()), HashMap::putAll);
        Map<UUID, Integer> orderItemQuantityMap = orderRequests.stream()
                .collect(HashMap::new, (map, item) -> map.put(item.getGroceryItemId(), item.getQuantity()), HashMap::putAll);

        for (Map.Entry<UUID, Integer> entry : orderItemQuantityMap.entrySet()) {
            if (groceryItemQuantityMap.get(entry.getKey()) < entry.getValue() ||
                    groceryItemQuantityMap.get(entry.getKey()) == 0 ||
                    0 > groceryItemQuantityMap.get(entry.getKey()) - orderItemQuantityMap.get(entry.getKey())) {
                return Map.of(entry.getKey(), groceryItemQuantityMap.get(entry.getKey()));
            }
        }

        return null;
    }

    /**
     * This method will return the grocery price from grocery id
     *
     * @param items list of grocery items
     * @param id    grocery id
     * @return BigDecimal grocery price
     */
    private BigDecimal getPriceFromGroceryItem(List<GroceryItems> items, UUID id) {
        GroceryItems item = items.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElse(GroceryItems.builder().price(BigDecimal.valueOf(0)).build());

        return item.getPrice();
    }


    /**
     * This method will return the grocery name from grocery id
     *
     * @param items list of grocery items
     * @param id    grocery id
     * @return String grocery name
     */
    private String getGroceryNameFromGroceryId(List<GroceryItems> items, UUID id) {
        GroceryItems item = items.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElse(GroceryItems.builder().price(BigDecimal.valueOf(0)).build());

        return item.getName();
    }


    /**
     * This method will process the order items and update the inventory
     *
     * @param orderRequests user requested grocery items
     * @param groceryItems  groceryItems from inventory
     * @param createdOrder  created order
     * @return List<OrderItems> list of order items
     */
    private List<OrderItems> processOrderItems(List<OrderRequest> orderRequests,
                                               List<GroceryItems> groceryItems,
                                               Orders createdOrder) {
        List<OrderItems> orderItems = new ArrayList<>();
        for (OrderRequest orderRequest : orderRequests) {

            BigDecimal price = getPriceFromGroceryItem(groceryItems, orderRequest.getGroceryItemId());

            GroceryItems item = groceryItems.stream()
                    .filter(e -> e.getId().equals(orderRequest.getGroceryItemId()))
                    .findFirst().orElse(GroceryItems.builder().build());

            // Update the quantity in inventory
            item.setQuantity(item.getQuantity() - orderRequest.getQuantity());
            groceryService.updateInventory(Map.of(item.getId(), item.getQuantity()));

            OrderItems orderItem = OrderItems.builder()
                    .orderId(createdOrder.getId())
                    .groceryItemId(orderRequest.getGroceryItemId())
                    .quantity(orderRequest.getQuantity())
                    .price(price.multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                    .build();

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private Payments createDefaultPendingPayment(Orders order) {

        PaymentsModel paymentsModel = PaymentsModel.builder()
                .orderId(order.getId())
                .status(Enums.PaymentStatus.PENDING)
                .build();

        GenericResponse<PaymentsModel> response = paymentsService.createPayment(paymentsModel);

        return PaymentsMapper.mapToPayments(response.getData());
    }

}
