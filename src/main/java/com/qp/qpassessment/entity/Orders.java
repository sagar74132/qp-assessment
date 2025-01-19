package com.qp.qpassessment.entity;

import com.qp.qpassessment.constant.Enums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Enums.OrderItemStatus status = Enums.OrderItemStatus.PENDING;

    @Column(name = "is_deleted")
    @Builder.Default
    private Integer isDeleted = 0;

    @ManyToOne(targetEntity = Users.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users user;

    @OneToMany(targetEntity = OrderItems.class, mappedBy = "id", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;

    @OneToMany(targetEntity = Payments.class, mappedBy = "id", cascade = CascadeType.ALL)
    private List<Payments> payments;
}
