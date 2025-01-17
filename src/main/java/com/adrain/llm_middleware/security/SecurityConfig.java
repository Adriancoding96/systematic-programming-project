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

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /*
   * Method configures API security filter chain
   *
   * @param http: Contains HttpSecurity object for web sucurity configurations
   * @param authenticationManager: Contains authentication manager that handles auth logic
   * @return SecurityFilterChain: Contains configured sercutity filter chain
   * @throws Exception: Exception gets thrown if any error occurs during secutiry filter chain configuration
   * */
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

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
  }
} 
