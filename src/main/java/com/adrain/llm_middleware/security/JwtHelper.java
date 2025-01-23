package com.adrain.llm_middleware.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.adrain.llm_middleware.exception.AccessDeniedException;
import com.adrain.llm_middleware.model.User;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


/**
 * Utility class for handling JWT tokens.
 * This class provides methods for generating, validating, and getting information from JWT tokens.
 * It uses the {@link Jwts} library for token creation and validation.
 *
 * @see Jwts
 */
public class JwtHelper {

  /**
   *
   * Static attributes defining the key generation algorithm, and
   * the standard time for jwt tokens to expire.
   *
   */
  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final int MINUTES = 60;

  /**
   * Generates a JWT token for the given email.
   *
   * @param email the email of the {@link User} to be set in the JWT token
   * @return the generated JWT token as a string
   */
  public static String generateToken(String email) {
    var now = Instant.now();
    return Jwts.builder()
      .subject(email)
      .issuedAt(Date.from(now))
      .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
      .compact();
  }

  /**
   * Extracts the {@link User}s username (email) from the given JWT token.
   *
   * @param token the JWT token
   * @return the username (email) extracted from the token
   */
  public static String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  /**
   * Validates the JWT token against the {@link UserDetails}.
   * The token is considered valid if:
   * <ul>
   *   <li>The username in the token matches the username is valid.</li>
   *   <li>The token is not expired.</li>
   * </ul>
   *
   * @param token the JWT token to validate
   * @param userDetails the user details to validate against
   * @return {@code true} if the token is valid, else {@code false}
   */
  public static boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token); 
  }

  /**
   * Extracts the claims from the given JWT token.
   *
   * @param token the JWT token
   * @return the claims extracted from the token
   * @throws AccessDeniedException if the token signature is invalid / the token has expired
   */
  private static Claims getTokenBody(String token) {
    try {
      return Jwts
        .parser()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (SignatureException | ExpiredJwtException e) {
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  /**
   * Checks if the given JWT token is expired.
   *
   * @param token the JWT token
   * @return {@code true} if the token is expired, else {@code false}
   */
  private static boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }

}
