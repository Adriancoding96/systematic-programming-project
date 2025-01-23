package com.adrain.llm_middleware.config;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Custom CORS (Cross-Origin Resource Sharing) configuration component.
 * This class implements {@link CorsConfigurationSource} to provide a custom
 * CORS configuration for incoming HTTP requests. It allows specifying allowed
 * origins, methods, headers, and other CORS-related settings.
 *
 * @see CorsConfigurationSource
 * @see CorsConfiguration
 */
@Component
public class CustomCorsConfiguration implements CorsConfigurationSource {

  /**
   * Provides a custom CORS configuration for HTTP requests.
   * This method defines the allowed origins, methods, headers, credentials,
   * and maximum age for CORS requests.
   *
   * @param request the incoming HTTP request
   * @return a {@link CorsConfiguration} object with the custom CORS settings
   */
  @Override
  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:5173"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);
    return config;
  }
}
