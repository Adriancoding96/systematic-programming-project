package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.LoginRequest;
import com.adrain.llm_middleware.record.auth.LoginResponse;
import com.adrain.llm_middleware.security.JwtHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for handling user login requests.
 * This controller authenticates {@link User} based on their email and password,
 * and generates a JSON web token upon successful authentication.
 *
 * @see RestController
 * @see AuthenticationManager
 * @see UsernamePasswordAuthenticationToken
 */
@RestController
public class LoginController {
  
  private final AuthenticationManager authenticationManager;

  @Autowired
  public LoginController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * Handles user login requests.
   * This method authenticates the user using the provided email and password,
   * and generates a JWT token on successful authentication.
   *
   * @param loginRequest the login request containing the {@link User} email and password
   * @return a {@link ResponseEntity} containing the {@link LoginResponse} with the {@link User} email and JWT token
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
    );
    String token = JwtHelper.generateToken(loginRequest.email());
    return ResponseEntity.ok(new LoginResponse(loginRequest.email(), token));
  }
  
}
