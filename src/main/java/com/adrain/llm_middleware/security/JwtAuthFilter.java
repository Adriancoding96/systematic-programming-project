package com.adrain.llm_middleware.security;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.adrain.llm_middleware.response.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Component is responsible for handling JWT authentication for incoming http requests.
 * This filter extends {@link OncePerRequestFilter} which ensures single execution.
 * It validates JWT tokens from the "Authorization" header and sets the authentication context
 * if the token is valid.
 *
 * <p>This filter excludes certain endpoints for example "/h2-console" from validation.</p>
 *
 * @see OncePerRequestFilter
 * @see JwtHelper
 * @see UserDetailsService
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private UserDetailsService userDetailsService;
  private ObjectMapper objectMapper;

  @Autowired
  public JwtAuthFilter(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
    this.userDetailsService = userDetailsService;
    this.objectMapper = objectMapper;
  }

   /**
   * Defines endpoints should be ignored by the authorizaiton filter.
   *
   * @param request the gttp request
   * @return {@code true} if the request should not be filtered, {@code false} otherwise
   * @throws ServletException if an error occurs from http request
   */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.startsWith("/h2-console") || path.equals("/favicon.ico");
  }


  /**
   * Processes the incoming request to validate the JWT token and set the authentication context.
   * This method extracts the token from the "Authorization" header, validates it using {@link JwtHelper},
   * and sets the authentication context if the token is valid.
   *
   * @param request the http request
   * @param response the http response
   * @param filterChain the filter chain containing the the authorition pipe / chain
   * @throws ServletException if an error occurs during the http request
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
    try {
      String authHeader = request.getHeader("Authorization");
      String token = null;
      String username = null;
      if(authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = JwtHelper.extractUsername(token);
      }

      if(token == null) {
        filterChain.doFilter(request, response);
        return;
      }

      if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(JwtHelper.validateToken(token, userDetails)) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    
      filterChain.doFilter(request, response);
    } catch (AccessDeniedException e) {
      ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      writeErrorResponse(response, errorResponse);
    } catch (IOException e) {
      ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "I/O error occurred");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      writeErrorResponse(response, errorResponse);
    }
  }
  

  /**
   * Writes an error response to the http response in a json format.
   *
   * @param response the http response
   * @param errorResponse the error response object to be created
   */
  private void writeErrorResponse(HttpServletResponse response, ApiErrorResponse errorResponse) {
      try {
          response.getWriter().write(toJson(errorResponse));
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**
   * Converts an {@link ApiErrorResponse} object to json format.
   *
   * @param response the error response object to convert
   * @return the json representation of the error response
   */
  private String toJson(ApiErrorResponse response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) { //TODO implement custom exception for json conversion
      return "";
    }
  }
}
