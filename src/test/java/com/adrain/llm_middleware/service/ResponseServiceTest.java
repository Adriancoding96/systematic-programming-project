package com.adrain.llm_middleware.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.mapper.ResponseMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.model.User;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.repository.ResponseRepository;
import com.adrain.llm_middleware.security.AuthenticationFacade;
import com.adrain.llm_middleware.service.impl.ResponseServiceImpl;
import com.adrain.llm_middleware.util.PromptResponseLinker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public class ResponseServiceTest {

  @Mock
  private ResponseRepository responseRepository;
  @Mock
  private ResponseMapper responseMapper;
  @Mock
  private AuthenticationFacade authenticationFacade;
  @Mock
  private UserService userService;
  @Mock
  private PromptResponseLinker promptResponseLinker;

  @InjectMocks
  private ResponseServiceImpl responseService;

  @BeforeEach
  void setupSecurityContext() {
    lenient().when(authenticationFacade.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("adrian@example.com", null));
  }

  @Test
  public void testNewResponse() {
    ResponseRecord record = new ResponseRecord(
        "Java23 is the latest stable jdk release",
        List.of("Java23", "jdk"),
        ResponseRating.VERY_USEFUL,
        "12345");

    User user = new User();
    user.setName("Adrian");
    user.setEmail("adrian@examle.com");
    user.setPassword("verysecure123");

    Prompt prompt = new Prompt();
    prompt.setPrompt("What is the latest jdk release");
    prompt.setUuid("12345");

    Response response = new Response();
    response.setResponseBody("Java23 is the latest stable jdk release");
    response.setUser(user);
    response.setPrompt(prompt);

    when(userService.getUserBySecurityContext()).thenReturn(user);
    when(promptResponseLinker.getPromptByUuid("12345")).thenReturn(prompt);
    when(responseMapper.toResponse(record)).thenReturn(response);
    when(responseRepository.save(response)).thenReturn(response);

    responseService.newResponse(record);
    verify(responseRepository, times(1)).save(response);
  }

  @Test
  public void testGetAllResponses() {
    Response response1 = new Response();
    response1.setResponseBody("This is the first response");
    Response response2 = new Response();
    response2.setResponseBody("This is the second response");

    when(responseRepository.findAll()).thenReturn(List.of(response1, response2));
    when(responseMapper.toRecord(response1)).thenReturn(new ResponseRecord(
          "This is the first response",
          List.of(),
          ResponseRating.USEFUL,
          "12345"));

    when(responseMapper.toRecord(response2)).thenReturn(new ResponseRecord(
          "This is the second response",
          List.of(),
          ResponseRating.USEFUL,
          "54321"));

    List<ResponseRecord> result = responseService.getAllResponses();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("This is the first response", result.get(0).responseBody());
    assertEquals("12345", result.get(0).promptUuid());
    assertEquals("This is the second response", result.get(1).responseBody());
    assertEquals("54321", result.get(1).promptUuid());
  }


  @Test
  public void testGetAllResponsesByUserEmail() {
    String email = "adrian@example.com";
    Response response1 = new Response();
    response1.setResponseBody("This is the first response");
    Response response2 = new Response();
    response2.setResponseBody("This is the second response");

    when(responseRepository.findAllByUserEmail(email)).thenReturn(List.of(response1, response2));
    when(responseMapper.toRecord(response1)).thenReturn(new ResponseRecord(
          "This is the first response",
          List.of(),
          ResponseRating.USEFUL,
          "12345"));

    when(responseMapper.toRecord(response2)).thenReturn(new ResponseRecord(
          "This is the second response",
          List.of(),
          ResponseRating.USEFUL,
          "54321"));

    List<ResponseRecord> result = responseService.getAllResponsesByUserEmail();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("This is the first response", result.get(0).responseBody());
    assertEquals("12345", result.get(0).promptUuid());
    assertEquals("This is the second response", result.get(1).responseBody());
    assertEquals("54321", result.get(1).promptUuid());
  }

  @Test
  public void testGetAllResponsesByResponseBodyAndUserEmail() {
    String responseBody = "Rust is a memory safe language";
    String email = "adrian@example.com";
    Response response1 = new Response();
    response1.setResponseBody("Rust is a memory safe language to a extent");
    Response response2 = new Response();
    response2.setResponseBody("Rust is a memory safe language thanks to ownership");

    when(responseRepository.searchByResponseBodyAndUserEmail(responseBody, email)).thenReturn(List.of(response1, response2));
    when(responseMapper.toRecord(response1)).thenReturn(new ResponseRecord(
          "Rust is a memory safe language to a extent",
          List.of(),
          ResponseRating.VERY_USEFUL,
          "12345"));

   when(responseMapper.toRecord(response2)).thenReturn(new ResponseRecord(
          "Rust is a memory safe language thanks to ownership",
          List.of(),
          ResponseRating.USEFUL,
          "54321"));

    List<ResponseRecord> result = responseService.findResponsesByResponseBodyAndUserEmail(responseBody);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Rust is a memory safe language to a extent", result.get(0).responseBody());
    assertEquals("12345", result.get(0).promptUuid());
    assertEquals("Rust is a memory safe language thanks to ownership", result.get(1).responseBody());
    assertEquals("54321", result.get(1).promptUuid());
  }

  @Test
  public void testGetResponseById() {
    Response response = new Response();
    response.setResponseBody("Alternatives to C++ are Rust, Zig and Odin");
    response.setMetaData(List.of("C++", "Rust", "Zig", "Odin"));
    response.setRating(ResponseRating.VERY_USEFUL);

    when(responseRepository.findById(1L)).thenReturn(Optional.of(response));
    when(responseMapper.toRecord(response)).thenReturn(new ResponseRecord(
          "Alternatives to C++ are Rust, Zig and Odin",
          List.of("C++", "Rust", "Zig", "Odin"),
          ResponseRating.VERY_USEFUL,
          "12345"));

    ResponseRecord result = responseService.getResponseById(1L);
    assertNotNull(result);
    assertEquals(result.responseBody(), "Alternatives to C++ are Rust, Zig and Odin");
    assertEquals(result.metaData().size(), 4);
    assertEquals(result.promptUuid(), "12345");
  }

  @Test
  public void testGetResponseByPromptId() {
    Response response = new Response();
    response.setResponseBody("Alternatives to C++ are Rust, Zig and Odin");
    response.setMetaData(List.of("C++", "Rust", "Zig", "Odin"));
    response.setRating(ResponseRating.VERY_USEFUL);

    when(responseRepository.findByPromptId(1L)).thenReturn(Optional.of(response));

    Response result = responseService.getResponseByPromptId(1L);
    assertNotNull(result);
    assertEquals(result.getResponseBody(), "Alternatives to C++ are Rust, Zig and Odin");
    assertEquals(result.getMetaData().size(), 4);
  }
}
