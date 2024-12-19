package com.adrain.llm_middleware.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
  
  /*
   * Gets authentication data from security context
   *
   * @return authentication: Object containing auth data
   * */
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
  
}
