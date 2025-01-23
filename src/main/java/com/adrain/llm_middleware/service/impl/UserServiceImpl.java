package com.adrain.llm_middleware.service.impl;

import com.adrain.llm_middleware.exception.ExistingUserException;
import com.adrain.llm_middleware.exception.UserNotFoundException;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.auth.SignupRequest;
import com.adrain.llm_middleware.repository.UserRepository;
import com.adrain.llm_middleware.security.AuthenticationFacade;
import com.adrain.llm_middleware.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service implementation for handling {@link User} database table operations.
 * This class implements the {@link UserService} interface and provides methods for
 * retrieving {@link User} data, registering new {@link User}s, and fetching the current
 * {@link User} from the {@link SecurityContextHolder} using {@link AuthenticationFacade}.
 *
 * <p>It uses the {@link UserRepository} for database operations, {@link PasswordEncoder} for password hashing,
 * and {@link AuthenticationFacade} to retrieve the current authenticated {@link User}.</p>
 *
 * @see UserService
 * @see UserRepository
 * @see PasswordEncoder
 * @see SecurityContextHolder
 * @see AuthenticationFacade
 */
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationFacade authenticationFacade;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationFacade = authenticationFacade;
  }

  /**
   * Retrieves a {@link User} by their email address.
   *
   * @param email the email address of the {@link User}
   * @return the {@link User} associated with the given email
   * @throws UserNotFoundException if no {@link User} is found with the given email
   */
  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException("User could not be found with email: " + email));
  }

  /**
   * Registers a new {@link User} based on the passed {@link SignupRequest}.
   * This method checks if a {@link User} with the given email already exists, encrypts the password,
   * and saves the new {@link User} to the database.
   *
   * @param signupRequest the request containing the user's registration details
   * @throws ExistingUserException if a user with the given email already exists
   */
  @Override
  public void signup(SignupRequest signupRequest) {
    String email = signupRequest.email();
    if(userRepository.existsByEmail(email)) {
      throw new ExistingUserException("User already exists with email: " + email);
    }
    String hashedPassword = passwordEncoder.encode(signupRequest.password());
    userRepository.save(new User(null, signupRequest.name(), email, hashedPassword, null, null));
  }

  /**
   * Fetches {@link User} from database using email, email is retrieved from the
   * {@link SecurityContextHolder} using {@link AuthenticationFacade}
   *
   * @return user {@link User} fetched from database
   * */
  @Override
  public User getUserBySecurityContext() { //TODO verify email string is not null
    String email = authenticationFacade.getAuthentication().getName();
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new UserNotFoundException("User could not be found with email: " + email));
  }
}
