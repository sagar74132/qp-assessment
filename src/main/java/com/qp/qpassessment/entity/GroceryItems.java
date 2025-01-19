package com.qp.qpassessment.entity;

import com.qp.qpassessment.constant.Enums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "grocery_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroceryItems {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private Enums.Category category;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "is_deleted", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer isDeleted = 0;

    @OneToMany(targetEntity = OrderItems.class, mappedBy = "id", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;
}
