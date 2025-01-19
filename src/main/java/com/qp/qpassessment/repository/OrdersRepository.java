package com.qp.qpassessment.repository;

import com.qp.qpassessment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, UUID> {

    Optional<Orders> findByUserId(UUID userId);
}
