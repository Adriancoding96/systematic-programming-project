package com.adrain.llm_middleware.record.auth;

import com.adrain.llm_middleware.model.User;

/**
 * Represents a request containing {@link User} auth details.
 * This record contains the email and password provided by the user during login process.
 *
 * @param email    the email address of the user
 * @param password the password of the user
 *
 * @see User
 */
public record LoginRequest(String email, String password) {
}
