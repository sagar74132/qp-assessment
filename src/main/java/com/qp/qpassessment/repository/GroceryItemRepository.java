package com.qp.qpassessment.repository;

import com.qp.qpassessment.entity.GroceryItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroceryItemRepository extends JpaRepository<GroceryItems, UUID>{

    Optional<List<GroceryItems>> findAllByIsDeleted(Integer isDeleted);

    Optional<GroceryItems> findByNameAndIsDeleted(String name, int isDeleted);

    Optional<List<GroceryItems>> findAllByIsDeletedAndQuantityGreaterThan(int idDeleted, int quantity);
}
