package com.adrain.llm_middleware.repository;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing {@link Response} rows in database table.
 *
 * <p>Custom queries are defined using Java Persistence Query Language to fetch responses
 * based on specific criteria such as {@link User} email, {@link Prompt} id, or response body.</p>
 *
 * @see JpaRepository
 * @see Response
 * @see User
 * @see Prompt
 * @see Optional
 */
public interface ResponseRepository extends JpaRepository<Response, Long> {

  /**
   * Finds all responses associated with a specific {@link User} email.
   *
   * @param email the email of the user
   * @return a list of {@link Response} objects associated with the given user email
   */
  @Query("SELECT r FROM Response r WHERE r.user.email = :email")
  List<Response> findAllByUserEmail(@Param("email") String email);

  /**
   * Finds a {@link Response} by the id of its associated {@link Prompt}.
   *
   * @param promptId the if of the {@link Prompt}
   * @return an {@link Optional} containing the {@link Response} if found
   */
  @Query("SELECT r FROM Response r WHERE r.prompt.id = :promptId")
  Optional<Response> findByPromptId(@Param("promptId") Long promptId);

  /**
   * Searches for {@link Response}s based sub string the response body and a specific {@link User} email.
   *
   * @param responseBody the sub string of the response body to search for
   * @param email the email of the {@link User}
   * @return a list of {@link Response} objects matching the sub string
   */
  @Query("SELECT r FROM Response r WHERE LOWER(r.responseBody) LIKE LOWER(CONCAT('%', :responseBody, '%')) AND r.user.email = :email")
  List<Response> searchByResponseBodyAndUserEmail(@Param("responseBody") String responseBody, @Param("email") String email);

}
