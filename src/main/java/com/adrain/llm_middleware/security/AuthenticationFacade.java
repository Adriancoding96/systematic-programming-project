package com.adrain.llm_middleware.security;

import com.adrain.llm_middleware.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Component is responsible for giving access to the current {@link Authentication} object
 * from the {@link SecurityContextHolder}.
 *
 * <p>It provides a method to retrieve the current {@link Authentication} object, which contains
 * details about the currently authenticated {@link User}.</p>
 *
 * @see Authentication
 * @see SecurityContextHolder
 * @see User
 */
@Component
public class AuthenticationFacade {
 
  /**
   * Retrieves the current {@link Authentication} object from the {@link SecurityContextHolder}.
   *
   * @return the current {@link Authentication} object, or {@code null} if no authentication is available
   */
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
  
}
