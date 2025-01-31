package com.adrain.llm_middleware.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
import com.adrain.llm_middleware.record.prompt.PromptRequest;
import com.adrain.llm_middleware.record.prompt.PromptResponse;
import com.adrain.llm_middleware.service.PromptService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link PromptController} class.
 *
 * <p>This test class defines unit tests for verifying functionalite of the endpoints of
 * {@link PromptController}. It uses {@link MockMvc} to simulate http requests and
 * validate the responses and ensuring that the controller interacts correctly with the
 * {@link PromptService}.</p>
 *
 * <p>The tests cover the following endpoints:
 * <ul>
 *   <li>{@code POST /api/prompt/new} - Tests the creation of a new {@link Prompt}.</li>
 *   <li>{@code GET /api/prompt} - Tests fetching all {@link PromptRecord}s.</li>
 *   <li>{@code GET /api/prompt/email/{email}} - Tests fetching {@link PromptRecord}s by {@link User} email.</li>
 *   <li>{@code GET /api/prompt/{id}} - Tests fetching a {@link PromptRecord} by its id.</li>
 *   <li>{@code DELETE /api/prompt/{id}} - Tests deleting a {@link PromptRecord} by its id.</li>
 * </ul>
 * </p>
 *
 * <p>Each test method is annotated with {@link WithMockUser} to simulate an authenticated user,
 * and the {@link MockMvc} instance is configured to include CSRF tokens in all requests to
 * ensure compatibility with Spring Security.</p>
 *
 * <p>This class uses the following key components:
 * <ul>
 *   <li>{@link MockMvc} - To simulate HTTP requests and validate responses.</li>
 *   <li>{@link MockBean} - To mock the {@link PromptService} and isolate the controller from
 *       external dependencies.</li>
 *   <li>{@link ObjectMapper} - To serialize and deserialize JSON.</li>
 *   <li>{@link WebApplicationContext} - To configure the Spring application context for testing.</li>
 * </ul>
 * </p>
 *
 * <p>All tests are executed with the {@code test} profile active, ensuring that the application
 * configuration is tailored for testing purposes.</p>
 *
 * @see PromptController
 * @see PromptService
 * @see PromptRequest
 * @see PromptResponse
 * @see PromptRecord
 * @see MockMvc
 * @see WithMockUser
 */
@WebMvcTest(PromptController.class)
@ActiveProfiles("test")
public class PromptControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PromptService promptService;

  @Autowired
  private ObjectMapper objectMapper;

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
 * Tests the {@code /api/prompt/new} endpoint to ensure it correctly processes a new {@link PromptRequest}
 * and returns a correct {@link PromptResponse}.
 *
 * <p>This test verifies the following:
 * <ul>
 *   <li>The endpoint accepts a valid {@link PromptRequest} object as JSON input.</li>
 *   <li>The endpoint returns an HTTP status code of 200 (OK).</li>
 *   <li>The response body contains the expected {@link PromptResponse} fields including:
 *     <ul>
 *       <li>The {@code response} field matches the expected response text.</li>
 *       <li>The {@code keywords} field contains the correct number of keywords and matches the expected values.</li>
 *       <li>The {@code uuid} field matches the expected uuid value.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This test uses MockMvc to simulate an HTTP request to the endpoint and performs assertions
 * on the response to ensure it meets the expected criteria.
 *
 * @throws Exception if an error occurs during the test execution, Specifically mapping to JSON using
 * {@link ObjectMapper}.
 */
  @Test
  @WithMockUser
  public void testNewPrompt() throws Exception {
    PromptRequest request = new PromptRequest("How do i center a div in html?", "deepseek-v3");
    PromptResponse response = new PromptResponse("You need to use html and css...", List.of("html", "css"), "awesome-uuid-3000");
    when(promptService.newPrompt(request)).thenReturn(response);

    mockMvc.perform(post("/api/prompt/new")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.response").value("You need to use html and css..."))
      .andExpect(jsonPath("$.keywords.length()").value(2))
      .andExpect(jsonPath("$.keywords[0]").value("html"))
      .andExpect(jsonPath("$.keywords[1]").value("css"))
      .andExpect(jsonPath("$.uuid").value("awesome-uuid-3000"));
  }

/**
 * Tests the {@code /api/prompt/} get endpoint to ensure it fetches a List of {@link PromptRecord}s
 *
 * <p>This test verifies the following:
 * <ul>
 *   <li>The endpoint returns an HTTP status code of 200 (OK).</li>
 *   <li>The response body contains the expected {@code List} of {@link PromptRecord} with fields including:
 *     <ul>
 *       <li>The {@code prompt} field matches the expected prompt text.</li>
 *       <li>The {@code uuid} field matches the expected uuid value.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This test uses MockMvc to simulate an HTTP request to the endpoint and performs assertions
 * on the response to ensure it meets the expected criteria.
 *
 * @throws Exception if an error occurs during the test execution, Specifically mapping to JSON using
 * {@link ObjectMapper}.
 */
  @Test
  @WithMockUser
  public void testGetAllPrompts() throws Exception {
    PromptRecord record1 = new PromptRecord("How do i center a div in html?", "123456");
    PromptRecord record2 = new PromptRecord("How do i program a program", "654321");
    PromptRecord record3 = new PromptRecord("Why is my unit tests failing", "401401");
    List<PromptRecord> records = List.of(record1, record2, record3);

    when(promptService.getAllPrompts()).thenReturn(records);
    mockMvc.perform(get("/api/prompt"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].prompt").value("How do i center a div in html?"))
      .andExpect(jsonPath("$[0].uuid").value("123456"))
      .andExpect(jsonPath("$[1].prompt").value("How do i program a program"))
      .andExpect(jsonPath("$[1].uuid").value("654321"))
      .andExpect(jsonPath("$[2].prompt").value("Why is my unit tests failing"))
      .andExpect(jsonPath("$[2].uuid").value("401401"));
  }

/**
 * Tests the {@code /api/prompt/email/{email}} get endpoint to ensure it fetches a List of {@link PromptRecord}s
 * related to provided {@link User} email.
 * <p>This test verifies the following:
 * <ul>
 *   <li>The endpoint returns an HTTP status code of 200 (OK).</li>
 *   <li>The response body contains the expected {@code List} of {@link PromptRecord} with fields including:
 *     <ul>
 *       <li>The {@code prompt} field matches the expected prompt text.</li>
 *       <li>The {@code uuid} field matches the expected uuid value.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This test uses MockMvc to simulate an HTTP request to the endpoint and performs assertions
 * on the response to ensure it meets the expected criteria.
 *
 * @throws Exception if an error occurs during the test execution, Specifically mapping to JSON using
 * {@link ObjectMapper}.
 *//*
  @Test
  @WithMockUser
  public void testGetAllPromptsByUserEmail() throws Exception {
    String email = "master.programmer.noob.unit.tester@infiniterecursion.com";
    PromptRecord record1 = new PromptRecord("How do i center a div in html?", "123456");
    PromptRecord record2 = new PromptRecord("How do i program a program", "654321");
    PromptRecord record3 = new PromptRecord("Why is my unit tests failing", "401401");
    List<PromptRecord> records = List.of(record1, record2, record3);

    when(promptService.getAllPromptsByUserEmail(email)).thenReturn(records);
    mockMvc.perform(get("/api/prompt/email/{email}", email))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].prompt").value("How do i center a div in html?"))
      .andExpect(jsonPath("$[0].uuid").value("123456"))
      .andExpect(jsonPath("$[1].prompt").value("How do i program a program"))
      .andExpect(jsonPath("$[1].uuid").value("654321"))
      .andExpect(jsonPath("$[2].prompt").value("Why is my unit tests failing"))
      .andExpect(jsonPath("$[2].uuid").value("401401"));
  }*/

/**
 * Tests the {@code /api/prompt/{id}} get endpoint to ensure it fetches a {@link PromptRecord}
 * related to provided {@link Prompt} id.
 * <p>This test verifies the following:
 * <ul>
 *   <li>The endpoint returns an HTTP status code of 200 (OK).</li>
 *   <li>The response body contains the expected {@link PromptRecord} with fields including:
 *     <ul>
 *       <li>The {@code prompt} field matches the expected prompt text.</li>
 *       <li>The {@code uuid} field matches the expected uuid value.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <p>This test uses MockMvc to simulate an HTTP request to the endpoint and performs assertions
 * on the response to ensure it meets the expected criteria.
 *
 * @throws Exception if an error occurs during the test execution, Specifically mapping to JSON using
 * {@link ObjectMapper}.
 */
  @Test
  @WithMockUser
  public void testGetPromptById() throws Exception {
    PromptRecord record = new PromptRecord("How do i write unit tests for unit tests", "1234321");

    when(promptService.getPromptById(1L)).thenReturn(record);
    mockMvc.perform(get("/api/prompt/{id}", 1L))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.prompt").value("How do i write unit tests for unit tests"))
      .andExpect(jsonPath("$.uuid").value("1234321"));
  }

/**
 * Tests the {@code /api/prompt/{id}} get endpoint to ensure it deletes a {@link PromptRecord}
 * related to provided {@link Prompt} id.
 * <p>This test verifies the following:
 * <ul>
 *   <li>The endpoint returns an HTTP status code of 200 (OK).</li>
 * </ul>
 *
 * <p>This test uses MockMvc to simulate an HTTP request to the endpoint and performs assertions
 * on the response to ensure it meets the expected criteria.
 *
 * @throws Exception if an error occurs during the test execution, Specifically mapping to JSON using
 * {@link ObjectMapper}.
 */
  @Test
  @WithMockUser
  public void testDeletePromptById() throws Exception {
    doNothing().when(promptService).deletePromptById(1L);
    mockMvc.perform(delete("/api/prompt/{id}", 1L))
      .andExpect(status().isOk());
  }


}
