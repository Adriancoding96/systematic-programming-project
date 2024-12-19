package com.adrain.llm_middleware.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.adrain.llm_middleware.exception.AccessDeniedException;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


public class JwtHelper {

  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final int MINUTES = 60;

  /*
   * Method to create a jwt token for authentication request
   *
   * @param email: users email
   * @return token: returns generated token as a string
   * */
  public static String generateToken(String email) {
    var now = Instant.now();
    return Jwts.builder()
      .subject(email)
      .issuedAt(Date.from(now))
      .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
      .compact();
  }

  /*
   * Helper method to extract username from jwt token
   *
   * @param token: jwt token
   * @return username: returns extracted username
   * */
  public static String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  /*
   * Method validates the token and the user it belongs to
   *
   * @param token: string representation of jwt token
   * @param userDetails: UserDetails object from spring.security.core
   * */
  public static boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token); 
  }

  /*
   * Method returns authentication claims extracted from jwt token
   *
   * @param token: string representation of jwt token
   * @return claims: return a claims object containing authentication information
   * -- docs: https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-claims
   * @throws SignatureException: throws exception when signature does not match local jwt signature
   * @throws ExpiredJwtException: throws exception when jwt token is expired
   * */
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

  /*
   * Helper method to check if jwt token has expired
   *
   * @param token: string representation of jwt token
   * @return boolean: returns true if token is expired. false if token is still valid
   * */
  private static boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }

}
