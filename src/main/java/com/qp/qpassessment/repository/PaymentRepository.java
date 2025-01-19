package com.qp.qpassessment.repository;


import com.qp.qpassessment.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, UUID> {

    Optional<Payments> findByOrderId(UUID orderId);
}
