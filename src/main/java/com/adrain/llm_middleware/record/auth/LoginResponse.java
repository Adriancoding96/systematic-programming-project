package com.adrain.llm_middleware.record.auth;

import com.adrain.llm_middleware.model.User;

/**
 * Represents a response containing data for authenticated {@link User}s .
 * This record contains the email and a token generated on successful authentication.
 *
 * @param email the email address of the authenticated user
 * @param token the JWT token generated for the authenticated {@link User}
 *
 * @see User
 */
public record LoginResponse(String email, String token) {
}
