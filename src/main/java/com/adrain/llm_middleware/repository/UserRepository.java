package com.adrain.llm_middleware.repository;

import java.util.Optional;

import com.adrain.llm_middleware.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} database table rows.
 *
 * <p>Custom query methods are provided to find a {@link User} by email
 * and check if a user exists with a given email.</p>
 *
 * @see JpaRepository
 * @see User
 * @see Optional
 */
public interface UserRepository extends JpaRepository<User, Long> {
  
  /**
   * Finds a {@link User} by their email address.
   *
   * @param email the email address of the user
   * @return an {@link Optional} containing the {@link User} if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Checks if a {@link User} exists with the given email address.
   *
   * @param email the email address to check
   * @return {@code true} if a user with the given email exists, else {@code false}
   */
  boolean existsByEmail(String email);
}
