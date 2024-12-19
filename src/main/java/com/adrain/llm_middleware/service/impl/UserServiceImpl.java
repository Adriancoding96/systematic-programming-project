package com.adrain.llm_middleware.service.impl;

import com.adrain.llm_middleware.exception.ExistingUserException;
import com.adrain.llm_middleware.exception.UserNotFoundException;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.SignupRequest;
import com.adrain.llm_middleware.repository.UserRepository;
import com.adrain.llm_middleware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }
 
  /*
   * Method fetches user from database by email
   *
   * @param email: Contains email of user to be fetches
   * @return user:  Contains user fetched from database
   * @throws UserNotFoundException: Throws exception if user with specified email not found in database
   * */
  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException("User could not be found with email: " + email));
  }


  /*
   * Method creates a new user if none already exists with the same email
   *
   * @param signupRequest: Record containing name, email, and password
   * @throws ExistingUserException: Throws exception if user with email already exists in database
   * */
  @Override
  public void signup(SignupRequest signupRequest) {
    String email = signupRequest.email();
    if(userRepository.existsByEmail(email)) {
      throw new ExistingUserException("User already exists with email: " + email);
    }
    String hashedPassword = passwordEncoder.encode(signupRequest.password());
    userRepository.save(new User(null, signupRequest.name(), email, hashedPassword));
  }


  
}
