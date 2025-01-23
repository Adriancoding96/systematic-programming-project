package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.SignupRequest;
import com.adrain.llm_middleware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling {@link User} signup requests.
 * This controller provides an endpoint for registering new users.
 *
 * @see RestController
 * @see UserService
 * @see SignupRequest
 */
@RestController
public class SignupController {
  
  private final UserService userService;

  @Autowired
  public SignupController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles requests to register a new {@link User}.
   *
   * @param signupRequest the request containing the {@link User} signup details
   * @return a {@link ResponseEntity} with HTTP status 201 (CREATED) upon successful registration
   */
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
    userService.signup(signupRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
}
