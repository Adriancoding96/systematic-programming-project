package com.adrain.llm_middleware.controller;

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

@RestController
public class LoginController {
  
  private final AuthenticationManager authenticationManager;

  @Autowired
  public LoginController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /*
   * Endpoint method for authenticating existing users
   *
   * @param loginRequest: Record containing user credentials
   * @return ResponseEntity<LoginResponse>: Response containing user email and generated jwt token
   * */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
    );
    String token = JwtHelper.generateToken(loginRequest.email());
    return ResponseEntity.ok(new LoginResponse(loginRequest.email(), token));
  }
  
}
