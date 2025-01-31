package com.adrain.llm_middleware.service.impl;

import com.adrain.llm_middleware.exception.UserNotFoundException;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user details during authentication.
 * This class implements the {@link UserDetailsService}.
 *
 * <p>It uses the {@link UserRepository} to fetch user details from the database and
 * constructs a {@link UserDetails} object for Spring Security.</p>
 *
 * @see UserDetailsService
 * @see UserRepository
 * @see UserDetails
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
 
  private final UserRepository userRepository;

  @Autowired
  public UserDetailServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Sets {@link UserDetails} by the {@link User} email address.
   * This method is used by Spring Security during the authentication process.
   *
   * @param email the email address of the {@link User}
   * @return the {@link UserDetails} object containing the {@link User} details
   * @throws UserNotFoundException if no {@link User} is found with the provided email
   */
  public UserDetails loadUserByUsername(String email) {
    User user = getUserByEmail(email);
    return org.springframework.security.core.userdetails.User.builder()
      .username(user.getEmail())
      .password(user.getPassword())
      .build();
  }


  /**
   * Fetches a {@link User} from the database by their email address.
   *
   * @param email the email address of the {@link User}
   * @return the {@link User} associated with the given email
   * @throws UserNotFoundException if no {@link User} is found with the given email
   */
  private User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException("Could not find user with email: " + email));
  }

}
