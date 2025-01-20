package com.adrain.llm_middleware.service;

import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.SignupRequest;

public interface UserService {
  User getUserByEmail(String email);
  void signup(SignupRequest signupRequest);
  User getUserBySecurityContext();
}
