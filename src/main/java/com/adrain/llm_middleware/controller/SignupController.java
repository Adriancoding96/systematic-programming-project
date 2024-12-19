package com.adrain.llm_middleware.controller;

import com.adrain.llm_middleware.record.auth.SignupRequest;
import com.adrain.llm_middleware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {
  
  private final UserService userService;

  @Autowired
  public SignupController(UserService userService) {
    this.userService = userService;
  }

  /*
   * Endpoint method for signing up new users
   *
   * @param signupRequest: Contains signup data, name, email, and password
   * @return ResponseEntity<Void>: Cotains http status code CREATED without a responsebody 
   * */
  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
    userService.signup(signupRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
  
}
