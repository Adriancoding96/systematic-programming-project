package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.SignupRequest;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Service interface for handling {@link User} database table related operations.
 * This interface defines methods for retrieving {@link User} details and registering new {@link Users}s.
 *
 * @see User
 * @see SignupRequest
 */
public interface UserService {

  /**
   * Retrieves a {@link User} by their email address.
   *
   * @param email the email address of the {@link User}
   * @return the {@link User} associated with the given email
   */
  User getUserByEmail(String email);

  /**
   * Registers a new {@link User} based on the provided {@link SignupRequest}.
   *
   * @param signupRequest the request containing the {@link User} details
   */
  void signup(SignupRequest signupRequest);

  /**
   * Retrieves the current {@link user} from the {@link SecurityContextHolder}.
   *
   * @return the {@link User} from the {@link SecurityContextHolder}
   */
  User getUserBySecurityContext();
}
