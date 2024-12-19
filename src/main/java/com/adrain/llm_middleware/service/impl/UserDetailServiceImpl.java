package com.adrain.llm_middleware.service.impl;

import com.adrain.llm_middleware.exception.UserNotFoundException;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
 
  private final UserRepository userRepository;

  @Autowired
  public UserDetailServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*
   * Method creates and returns spring.core.UserDetails.User from a model.User object
   *
   * @param email: Contains user email
   * @return UserDetails: Contains object constructed from user data
   * */
  public UserDetails loadUserByUsername(String email) {
    User user = getUserByEmail(email);
    return org.springframework.security.core.userdetails.User.builder()
      .username(user.getEmail())
      .password(user.getPassword())
      .build();
  }

  /*
   * Method fetches user from database by email
   *
   * @param email: Contains user email
   * @return user: User object fetched from the database
   * @throws UserNotFoundException: Throws exception if no user found by email
   * */
  private User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
  }

}
