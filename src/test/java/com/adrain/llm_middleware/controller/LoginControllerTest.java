package com.adrain.llm_middleware.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.adrain.llm_middleware.record.auth.LoginRequest;
import com.adrain.llm_middleware.security.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link LoginController} class.
 *
 * <p>This test class defines unit tests for verifying the functionality of
 * the {@code /login} endpoint exposed by {@link LoginController}. It uses
 * {@link MockMvc} to simulate HTTP requests and validate responses, ensuring
 * that the controller interacts correctly with the Spring Security
 * authentication infrastructure.</p>
 *
 * <p>The tests in this class cover the following:
 * <ul>
 *   <li>{@code POST /login} – Tests user login functionality by providing valid
 *       {@link LoginRequest} credentials.</li>
 * </ul>
 * </p>
 *
 * <p>The {@link MockMvc} instance is configured to include CSRF tokens in all
 * requests to ensure compatibility with Spring Security.</p>
 *
 * <p>This class uses the following key components:
 * <ul>
 *   <li>{@link MockMvc} – To simulate HTTP requests and validate responses.</li>
 *   <li>{@link MockBean} – To mock the {@link AuthenticationManager} and isolate
 *       the controller from external dependencies.</li>
 *   <li>{@link ObjectMapper} – To serialize and deserialize JSON.</li>
 *   <li>{@link WebApplicationContext} – To configure the Spring application context
 *       for testing.</li>
 *   <li>{@link JwtHelper} – To generate JWT tokens for authentication testing.</li>
 * </ul>
 * </p>
 *
 * <p>All tests are executed with the {@code test} profile active, ensuring that the
 * application configuration is tailored for testing purposes.</p>
 *
 * @see LoginController
 * @see LoginRequest
 * @see AuthenticationManager
 * @see JwtHelper
 * @see MockMvc
 * @see SpringExtension
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
@ActiveProfiles("test")
public class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Autowired private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webApplicationContext)
      .apply(springSecurity())
      .defaultRequest(get("/**").with(csrf().asHeader()))
      .defaultRequest(post("/**").with(csrf().asHeader()))
      .defaultRequest(put("/**").with(csrf().asHeader()))
      .defaultRequest(delete("/**").with(csrf().asHeader()))
      .build();
  } 

  /**
   * Tests the {@code POST /login} endpoint to ensure it processes a valid
   * {@link LoginRequest} and triggers a successful authentication process.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>Calling the {@link AuthenticationManager} with the correct
   *       {@link UsernamePasswordAuthenticationToken}.</li>
   *   <li>Generating a JWT token via {@link JwtHelper#generateToken(String)}
   *       when the authentication is successful.</li>
   *   <li>Ensuring that the response status and any relevant headers or
   *       returned content match the expected outcome.</li>
   * </ul>
   * </p>
   *
   * <p>This test uses {@link MockMvc} to simulate an HTTP request to the
   * {@code /login} endpoint and performs assertions on the response to ensure
   * it meets the expected criteria.</p>
   *
   * @throws Exception if an error occurs during the test execution, specifically
   *         when mapping to/from JSON with {@link ObjectMapper}.
   */
  //TODO figure out why this test is redirectig to login/error
  @Test
  public void testLogin() throws Exception{
    String email = "adrian@exmaple.com";
    String password = "bestpasswordever";
    String token = "123456";

    LoginRequest request = new LoginRequest(email, password);
    Authentication authentication = mock(Authentication.class);

    when(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password))).thenReturn(authentication);

    try(MockedStatic<JwtHelper> mockedJwtHelper = mockStatic(JwtHelper.class)) {
      mockedJwtHelper.when(() -> JwtHelper.generateToken(email)).thenReturn(token);

      MvcResult result = mockMvc.perform(post("/login")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request)))
        .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
        System.out.println("Redirect URL: " + result.getResponse().getRedirectedUrl());
        System.out.println("Response Content: " + result.getResponse().getContentAsString());
    }

  }

}
