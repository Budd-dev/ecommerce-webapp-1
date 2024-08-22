package com.example.ECommerce.repository;

import com.example.ECommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNo(String phoneNo);
    boolean existsByPhoneNo(String phoneNo);
    boolean existsByEmail(String email);
}
