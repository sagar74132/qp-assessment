package com.qp.qpassessment.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Enums {

    public enum Role {
        ADMIN,
        USER
    }

    enum OrderStatus {
        PLACED,
        DELIVERED,
        CANCELLED
    }

    public enum Category {
        Fruits,
        Vegetables,
        Dairy,
        Snacks,
        Sweets
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        FAILED
    }

    public enum OrderItemStatus {
        PENDING,
        PLACED,
        DELIVERED,
        CANCELLED
    }
}
