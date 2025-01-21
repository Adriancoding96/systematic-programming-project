package com.adrain.llm_middleware.repository;

import java.util.Optional;
import java.util.stream.Stream;

import com.adrain.llm_middleware.model.Prompt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * PromptRepository is a repository interface for managing {@link Prompt} entities in the database.
 * <p>
 * Provides methods to perform CRUD operations and custom queries on {@code Prompt} objects.
 * </p>
 */
public interface PromptRepository extends JpaRepository<Prompt, Long> {

  /**
   * Retrieves all {@link Prompt} entities associated with a user email.
   * <p>
   *     The query selects all prompts where the {@code user.email} matches
   *     the specified {@code email}.
   * </p>
   *
   * @param email The email of the user whose prompts are to be fetched.
   * @return A stream of {@code Prompt} entities matching the users email.
   */
  @Query("SELECT p FROM Prompt p WHERE p.user.email = :email")
  Stream<Prompt> findAllByUserEmail(@Param("email") String email);
  
  /**
   * Retrieves {@link Prompt} with specified uuid.
   * <p>
   *     The query fetches {@link Prompt} with specified uuid.
   * </p>
   *
   * @param uuid of database row.
   * @return A {@link Prompt} entity matching the uuid.
   */
  @Query("SELECT p FROM Prompt p WHERE p.uuid = :uuid")
  Optional<Prompt> findByUuid(@Param("uuid") String uuid);
}
