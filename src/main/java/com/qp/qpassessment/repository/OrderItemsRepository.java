package com.qp.qpassessment.repository;


import com.qp.qpassessment.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, UUID> {

    Optional<OrderItems> findByOrderId(UUID orderId);
}
