package com.qp.qpassessment.entity;

import com.qp.qpassessment.constant.Enums;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Enums.PaymentStatus status = Enums.PaymentStatus.PENDING;

    @Column(name = "payment_date")
    @CreationTimestamp
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Orders order;
}
