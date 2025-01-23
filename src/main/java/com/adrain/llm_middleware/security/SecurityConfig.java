package com.adrain.llm_middleware.security;

import com.adrain.llm_middleware.config.CustomCorsConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration class for setting up Spring Security.
 * This class configures the security filter chain, authentication manager, and password encoder.
 * It also enables web security and configures CORS settings using custom {@link CustomCorsConfiguration}.
 *
 * <p>The security filter chain disables CSRF protection, and configures endpoint access rules.
 * It also integrates the {@link JwtAuthFilter} for JWT token authentication.</p>
 *
 * @see EnableWebSecurity
 * @see SecurityFilterChain
 * @see AuthenticationManager
 * @see PasswordEncoder
 * @see JwtAuthFilter
 * @see CustomCorsConfiguration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserDetailsService userDetailsService;
  private final JwtAuthFilter jwtAuthFilter;
  private final CustomCorsConfiguration customCorsConfiguration;

  @Autowired
  public SecurityConfig(UserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter, CustomCorsConfiguration customCorsConfiguration) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthFilter = jwtAuthFilter;
    this.customCorsConfiguration = customCorsConfiguration;
  }

  /**
   * Configures and provides a {@link PasswordEncoder} bean.
   * This method uses {@link BCryptPasswordEncoder} for encoding passwords.
   *
   * @return the configured {@link PasswordEncoder} bean
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  
  /**
   * Configures the security filter chain for the API.
   * This method sets up access rules for endpoints, disables csrf protection, configures cors,
   * and uses the {@link JwtAuthFilter} for JWT token authentication.
   *
   * @param http the {@link HttpSecurity} object for configuring web security
   * @param authenticationManager the {@link AuthenticationManager} for handling authentication logic
   * @return the configured {@link SecurityFilterChain}
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/favicon.ico").permitAll()
        .requestMatchers(HttpMethod.POST, "/signup/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/authentication-docs/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/test/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/test/**").permitAll()
        .anyRequest().authenticated())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authenticationManager(authenticationManager)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
      .cors(c -> c.configurationSource(customCorsConfiguration));

    return http.build();
  }

  /**
   * Configures and provides an {@link AuthenticationManager} bean.
   * This method sets up the authentication manager with the provided {@link UserDetailsService}
   * and {@link PasswordEncoder}.
   *
   * @param http the {@link HttpSecurity} object for configuring web security
   * @return the configured {@link AuthenticationManager} bean
   * @throws Exception if an error occurs during configuration
   */
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }
} 
