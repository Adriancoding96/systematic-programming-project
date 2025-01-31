package com.adrain.llm_middleware.record.auth;

import com.adrain.llm_middleware.model.User;

/**
 * Represents a request containing {@link User} registration details.
 * This record contains the name, email, and password provided by the {@link User}.
 *
 * @param name     the name of the user
 * @param email    the email address of the user
 * @param password the password of the user
 *
 * @see User
 */
public record SignupRequest(String name, String email, String password) {
}
