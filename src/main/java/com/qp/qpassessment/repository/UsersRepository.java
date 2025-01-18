package com.qp.qpassessment.repository;

import com.qp.qpassessment.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID>{

    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndPassword(String email, String password);
}
