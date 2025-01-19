package com.qp.qpassessment.service.impl;


import com.qp.qpassessment.constant.Enums;
import com.qp.qpassessment.entity.GroceryItems;
import com.qp.qpassessment.entity.OrderItems;
import com.qp.qpassessment.entity.Orders;
import com.qp.qpassessment.entity.Payments;
import com.qp.qpassessment.exception.OrdersException;
import com.qp.qpassessment.mapper.PaymentsMapper;
import com.qp.qpassessment.model.OrderItemsResponseDto;
import com.qp.qpassessment.model.OrderRequestDto;
import com.qp.qpassessment.model.OrderResponseDto;
import com.qp.qpassessment.model.PaymentsDto;
import com.qp.qpassessment.repository.OrderItemsRepository;
import com.qp.qpassessment.repository.OrdersRepository;
import com.qp.qpassessment.service.OrderService;
import com.qp.qpassessment.utils.AppConfig;
import com.qp.qpassessment.utils.GenericResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
    public GenericResponse<OrderResponseDto> placeOrder(List<OrderRequestDto> orderRequestDtos) {

        List<GroceryItems> groceryItems = groceryService.getGroceryItemsById(orderRequestDtos.stream()
                .map(OrderRequestDto::getGroceryItemId)
                .toList()
        );

        if (groceryItems.isEmpty() || groceryItems.size() != orderRequestDtos.size()) {
            throw new OrdersException(appConfig.getProperty("order.invalid.items"));
        }

        // Throw error if the requested quantity is greater than the available quantity
        Map<UUID, Integer> invalidGroceryItem = validateGroceryQuantity(orderRequestDtos, groceryItems);
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
        List<OrderItems> orderItems = processOrderItems(orderRequestDtos, groceryItems, createdOrder);


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

        List<OrderItemsResponseDto> orderItemsResponsDtos = orderItems.stream()
                .map(e -> OrderItemsResponseDto.builder()
                        .groceryItemId(e.getGroceryItemId())
                        .groceryName(getGroceryNameFromGroceryId(groceryItems, e.getGroceryItemId()))
                        .quantity(e.getQuantity())
                        .price(e.getPrice())
                        .build())
                .toList();

        OrderResponseDto response = OrderResponseDto.builder()
                .orderId(createdOrder.getId())
                .orderItems(orderItemsResponsDtos)
                .createdAt(createdOrder.getCreatedAt())
                .totalPrice(createdOrder.getTotalPrice())
                .paymentId(payment.getId())
                .build();

        return GenericResponse.<OrderResponseDto>builder()
                .data(response)
                .message(appConfig.getProperty("order.place.success", order.getId()))
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public GenericResponse<OrderResponseDto> getUserOrderList() {
        return null;
    }

    @Override
    public GenericResponse<OrderResponseDto> getOrderById() {
        return null;
    }

    /**
     * This method will validate the grocery quantity
     *
     * @param orderRequestDtos user requested grocery items
     * @param groceryItems  groceryItems from inventory
     * @return Map<UUID, Integer> if found any quantity invalid for a grocery otherwise null.
     */
    private Map<UUID, Integer> validateGroceryQuantity(List<OrderRequestDto> orderRequestDtos, List<GroceryItems> groceryItems) {
        Map<UUID, Integer> groceryItemQuantityMap = groceryItems.stream()
                .collect(HashMap::new, (map, item) -> map.put(item.getId(), item.getQuantity()), HashMap::putAll);
        Map<UUID, Integer> orderItemQuantityMap = orderRequestDtos.stream()
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
     * @param orderRequestDtos user requested grocery items
     * @param groceryItems  groceryItems from inventory
     * @param createdOrder  created order
     * @return List<OrderItems> list of order items
     */
    private List<OrderItems> processOrderItems(List<OrderRequestDto> orderRequestDtos,
                                               List<GroceryItems> groceryItems,
                                               Orders createdOrder) {
        List<OrderItems> orderItems = new ArrayList<>();
        for (OrderRequestDto orderRequestDto : orderRequestDtos) {

            BigDecimal price = getPriceFromGroceryItem(groceryItems, orderRequestDto.getGroceryItemId());

            GroceryItems item = groceryItems.stream()
                    .filter(e -> e.getId().equals(orderRequestDto.getGroceryItemId()))
                    .findFirst().orElse(GroceryItems.builder().build());

            // Update the quantity in inventory
            item.setQuantity(item.getQuantity() - orderRequestDto.getQuantity());
            groceryService.updateInventory(Map.of(item.getId(), item.getQuantity()));

            OrderItems orderItem = OrderItems.builder()
                    .orderId(createdOrder.getId())
                    .groceryItemId(orderRequestDto.getGroceryItemId())
                    .quantity(orderRequestDto.getQuantity())
                    .price(price.multiply(BigDecimal.valueOf(orderRequestDto.getQuantity())))
                    .build();

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private Payments createDefaultPendingPayment(Orders order) {

        PaymentsDto paymentsDto = PaymentsDto.builder()
                .orderId(order.getId())
                .status(Enums.PaymentStatus.PENDING)
                .build();

        GenericResponse<PaymentsDto> response = paymentsService.createPayment(paymentsDto);

        return PaymentsMapper.mapToPayments(response.getData());
    }

}
