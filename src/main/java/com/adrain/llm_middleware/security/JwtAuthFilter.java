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


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private UserDetailsService userDetailsService;
  private ObjectMapper objectMapper;

  @Autowired
  public JwtAuthFilter(UserDetailsService userDetailsService, ObjectMapper objectMapper) {
    this.userDetailsService = userDetailsService;
    this.objectMapper = objectMapper;
  }

  /*
   * Method defines endpoints that does not need jwt token headers
   *
   * @param request: contains http request
   * @return boolean: returns true if request is directed to one of specified endpoints not to be filtered
   * returns false if enpoint is not specified in method.
   *
   * */
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    return path.startsWith("/h2-console") || path.equals("/favicon.ico");
  }


  /*
   * Method extracts data from jwt token from authorization header core auth logic is handeled within
   * JwtHelper.
   *
   * @param request: contains http request
   * @param response: contains initial response from API
   * @param filterChain: FilterChain object that contains a chain of filtered requests
   *
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
  
  /*
   * Helper method to create an ApiErrorResponse JSON representation if error occurs in calling method
   *
   * @param response: contains initial API response
   * @param errorResponse: contains ApiRerrorResponse object
   * */
  private void writeErrorResponse(HttpServletResponse response, ApiErrorResponse errorResponse) {
      try {
          response.getWriter().write(toJson(errorResponse));
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /*
   * Helper method to convert ApiErrorResponse to JSON
   *
   * @param response: contains ApiErrorResponse object
   * @return json: return ApiErrorResponse as json
   * @Exception e: if exception is encountered returns empty string
   * */
  private String toJson(ApiErrorResponse response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) { //TODO implement custom exception for json conversion
      return "";
    }
  }
}
