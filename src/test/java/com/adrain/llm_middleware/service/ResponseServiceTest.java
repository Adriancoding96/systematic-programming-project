package com.adrain.llm_middleware.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
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

/**
 * Unit tests for the {@link ResponseServiceImpl} class.
 *
 * <p>This test class uses the {@link MockitoExtension} to create mock instances of
 * dependencies required by {@link ResponseServiceImpl}, isolating the service from
 * external components such as the {@link ResponseRepository}, {@link ResponseMapper},
 * and {@link UserService}. The goal is to verify the business logic and interactions
 * with these dependencies in a controlled (mocked) environment.</p>
 *
 * <p>The following mocks are used:
 * <ul>
 *   <li>{@link ResponseRepository} – Database access for {@link Response} entities.</li>
 *   <li>{@link ResponseMapper} – Converts between {@link Response} domain objects and
 *       {@link ResponseRecord} DTOs.</li>
 *   <li>{@link AuthenticationFacade} – Provides authentication details for the
 *       currently logged-in user.</li>
 *   <li>{@link UserService} – Retrieves user details from the security context.</li>
 *   <li>{@link PromptResponseLinker} – Fetches prompt details and links them with responses.</li>
 * </ul>
 * </p>
 *
 * <p>Tests in this class include:
 * <ul>
 *   <li>{@link #testNewResponse()} – Ensures a new {@link Response} is saved correctly when
 *       created from a {@link ResponseRecord}.</li>
 *   <li>{@link #testGetAllResponses()} – Ensures all stored responses are retrieved and
 *       mapped to {@link ResponseRecord} objects.</li>
 *   <li>{@link #testGetAllResponsesByUserEmail()} – Ensures that responses filtered by
 *       the authenticated user's email are retrieved correctly.</li>
 *   <li>{@link #testGetAllResponsesByResponseBodyAndUserEmail()} – Ensures that
 *       responses can be searched by a partial {@code responseBody} string and user email.</li>
 *   <li>{@link #testGetResponseById()} – Ensures a response is correctly retrieved
 *       by its ID and mapped to a {@link ResponseRecord}.</li>
 *   <li>{@link #testGetResponseByPromptId()} – Ensures a response is correctly retrieved
 *       by the related prompt ID.</li>
 *   <li>{@link #testDeleteResponseById()} – Ensures a response is properly deleted by
 *       its ID.</li>
 * </ul>
 * </p>
 *
 * @see ResponseService
 * @see ResponseServiceImpl
 * @see ResponseRepository
 * @see ResponseMapper
 * @see MockitoExtension
 */
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

  /**
   * Tests {@link ResponseServiceImpl#newResponse(ResponseRecord)} to ensure a new
   * {@link Response} is created and saved correctly using data from a {@link ResponseRecord}.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The current user is fetched from {@link UserService#getUserBySecurityContext()}.</li>
   *   <li>The related {@link Prompt} is retrieved by UUID via {@link PromptResponseLinker}.</li>
   *   <li>The {@link Response} is correctly mapped from the provided {@link ResponseRecord}
   *       and saved to the repository.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#getAllResponses()} to ensure it retrieves all
   * stored {@link Response} entities and maps them correctly to {@link ResponseRecord}
   * objects.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The repository returns the expected list of {@link Response} entities.</li>
   *   <li>Each {@link Response} is mapped to a corresponding {@link ResponseRecord}
   *       with the correct fields.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#getAllResponsesByUserEmail()} to ensure it fetches
   * all {@link Response} entities for the currently authenticated user, mapped to
   * {@link ResponseRecord} objects.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The repository returns the expected list of {@link Response} entities filtered by user email.</li>
   *   <li>Each {@link Response} is mapped correctly to a {@link ResponseRecord}.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#findResponsesByResponseBodyAndUserEmail(String)} to
   * ensure it fetches all {@link Response} entities matching a given substring in the
   * {@code responseBody}, filtered by the authenticated user's email, and maps them to
   * {@link ResponseRecord} objects.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The repository returns the expected list of {@link Response} entities based on the search query.</li>
   *   <li>Each {@link Response} is mapped correctly to a {@link ResponseRecord}.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#getResponseById(Long)} to ensure that a
   * {@link Response} is retrieved by its ID and mapped correctly to a
   * {@link ResponseRecord}.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The repository returns the expected {@link Response} entity for the given ID.</li>
   *   <li>The returned {@link Response} is mapped correctly to a {@link ResponseRecord}.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#getResponseByPromptId(Long)} to ensure that
   * a {@link Response} is retrieved by its associated prompt ID.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The repository returns the expected {@link Response} entity for the given prompt ID.</li>
   *   <li>The returned {@link Response} object has the expected fields.</li>
   * </ul>
   * </p>
   */
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

  /**
   * Tests {@link ResponseServiceImpl#deleteResponseById(Long)} to ensure that
   * a {@link Response} is properly deleted from the repository by its ID.
   *
   * <p>This test verifies:
   * <ul>
   *   <li>The {@link ResponseRepository#deleteById(Long)} method is called once
   *       with the correct ID.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testDeleteResponseById() {
    doNothing().when(responseRepository).deleteById(1L);
    responseService.deleteResponseById(1L);
    verify(responseRepository, times(1)).deleteById(1L);
  }
}
