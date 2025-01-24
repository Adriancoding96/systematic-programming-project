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
