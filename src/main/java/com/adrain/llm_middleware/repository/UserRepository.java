package com.adrain.llm_middleware.repository;

import java.util.Optional;

import com.adrain.llm_middleware.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
}
