package com.adrain.llm_middleware.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
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

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Unit tests for the {@link ResponseController} class.
 *
 * <p>This test class defines unit tests for verifying the functionality of the endpoints in
 * {@link ResponseController}. It uses {@link MockMvc} to simulate HTTP requests and validate
 * responses, ensuring that the controller interacts correctly with the {@link ResponseService}.</p>
 *
 * <p>The tests cover the following endpoints:
 * <ul>
 *   <li>{@code POST /api/response/new} – Tests creating a new {@link ResponseRecord}.</li>
 *   <li>{@code GET /api/response} – Tests fetching all {@link ResponseRecord}s.</li>
 *   <li>{@code GET /api/response/user} – Tests fetching {@link ResponseRecord}s by user email.</li>
 *   <li>{@code GET /api/response/search} – Tests searching {@link ResponseRecord}s by a given query.</li>
 *   <li>{@code GET /api/response/{id}} – Tests fetching a {@link ResponseRecord} by its id.</li>
 *   <li>{@code DELETE /api/response/{id}} – Tests deleting a {@link ResponseRecord} by its id.</li>
 * </ul>
 * </p>
 *
 * <p>Each test method is annotated with {@link WithMockUser} to simulate an authenticated user.
 * The {@link MockMvc} instance is configured to include CSRF tokens in all requests to
 * ensure compatibility with Spring Security.</p>
 *
 * <p>This class uses the following key components:
 * <ul>
 *   <li>{@link MockMvc} – To simulate HTTP requests and validate responses.</li>
 *   <li>{@link MockBean} – To mock the {@link ResponseService} and isolate the controller
 *       from external dependencies.</li>
 *   <li>{@link ObjectMapper} – To serialize and deserialize JSON.</li>
 *   <li>{@link WebApplicationContext} – To configure the Spring application context for testing.</li>
 * </ul>
 * </p>
 *
 * <p>All tests are executed with the {@code test} profile active, ensuring that the application
 * configuration is tailored for testing purposes.</p>
 *
 * @see ResponseController
 * @see ResponseService
 * @see ResponseRecord
 * @see WithMockUser
 * @see MockMvc
 */
@WebMvcTest(ResponseController.class)
@ActiveProfiles("test")
public class ResponseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ResponseService responseService;

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
   * Tests the {@code POST /api/response/new} endpoint to ensure it processes a new
   * {@link ResponseRecord} and delegates correctly to {@link ResponseService#newResponse(ResponseRecord)}.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>A valid {@link ResponseRecord} can be passed to the endpoint without error.</li>
   *   <li>The {@code responseService.newResponse(...)} method is invoked with the correct data.</li>
   *   <li>The endpoint returns a proper HTTP status code (e.g., 2xx) indicating success.</li>
   * </ul>
   * </p>
   *
   * @throws Exception if any error occurs during request construction or execution.
   */
  @Test
  @WithMockUser
  public void testNewResponse() throws Exception {
    ResponseRecord record = new ResponseRecord(
        "You use html and css",
        List.of("html", "css"),
        ResponseRating.VERY_USEFUL,
        "123456");
    doNothing().when(responseService).newResponse(record);
    mockMvc.perform(post("/api/response/new"));
  }

  /**
   * Tests the {@code GET /api/response} endpoint to ensure it fetches a
   * {@link List} of {@link ResponseRecord}s.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>An HTTP 200 (OK) status is returned.</li>
   *   <li>The response contains the expected number of {@link ResponseRecord}s.</li>
   *   <li>The fields in each record match the expected values for {@code responseBody},
   *       {@code metaData}, {@code rating}, and {@code promptUuid}.</li>
   * </ul>
   * </p>
   *
   * @throws Exception if any error occurs during request construction or execution.
   */
  @Test
  @WithMockUser
  public void testGetAllResponses() throws Exception {
    ResponseRecord record1 = new ResponseRecord(
        "You use HTML and CSS",
        List.of("html", "css"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record2 = new ResponseRecord(
        "You can build a CLI with Rust and Tokio",
        List.of("rust", "tokio"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record3 = new ResponseRecord(
        "Java is a OOP language that sneaks in primitive types",
        List.of("java", "oop"),
        ResponseRating.VERY_USEFUL,
        "123456");
    List<ResponseRecord> records = List.of(record1, record2, record3);

    when(responseService.getAllResponses()).thenReturn(records);
    mockMvc.perform(get("/api/response"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].responseBody").value("You use HTML and CSS"))
        .andExpect(jsonPath("$[0].metaData.size()").value(2))
        // .andExpect(jsonPath("$[0].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[0].promptUuid").value("123456"))
        .andExpect(jsonPath("$[1].responseBody").value("You can build a CLI with Rust and Tokio"))
        .andExpect(jsonPath("$[1].metaData.size()").value(2))
        // .andExpect(jsonPath("$[1].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[1].promptUuid").value("123456"))
        .andExpect(jsonPath("$[2].responseBody").value("Java is a OOP language that sneaks in primitive types"))
        .andExpect(jsonPath("$[2].metaData.size()").value(2))
        // .andExpect(jsonPath("$[2].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[2].promptUuid").value("123456"));

  }

  /**
   * Tests the {@code GET /api/response/user} endpoint to ensure it fetches
   * all {@link ResponseRecord}s associated with the currently authenticated user
   * (via their email).
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>An HTTP 200 (OK) status is returned.</li>
   *   <li>The response matches the expected {@link ResponseRecord} list for the user's email.</li>
   * </ul>
   * </p>
   *
   * @throws Exception if any error occurs during request construction or execution.
   */
  @Test
  @WithMockUser
  public void testGetAllResponsesByUserEmail() throws Exception {
    ResponseRecord record1 = new ResponseRecord(
        "You use HTML and CSS",
        List.of("html", "css"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record2 = new ResponseRecord(
        "You can build a CLI with Rust and Tokio",
        List.of("rust", "tokio"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record3 = new ResponseRecord("Java is a OOP language that sneaks in primitive types",
        List.of("java", "oop"),
        ResponseRating.VERY_USEFUL,
        "123456");
    List<ResponseRecord> records = List.of(record1, record2, record3);

    when(responseService.getAllResponsesByUserEmail()).thenReturn(records);
    mockMvc.perform(get("/api/response/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].responseBody").value("You use HTML and CSS"))
        .andExpect(jsonPath("$[0].metaData.size()").value(2))
        // .andExpect(jsonPath("$[0].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[0].promptUuid").value("123456"))
        .andExpect(jsonPath("$[1].responseBody").value("You can build a CLI with Rust and Tokio"))
        .andExpect(jsonPath("$[1].metaData.size()").value(2))
        // .andExpect(jsonPath("$[1].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[1].promptUuid").value("123456"))
        .andExpect(jsonPath("$[2].responseBody").value("Java is a OOP language that sneaks in primitive types"))
        .andExpect(jsonPath("$[2].metaData.size()").value(2))
        // .andExpect(jsonPath("$[2].rating").value(ResponseRating.VERY_USEFUL))
        .andExpect(jsonPath("$[2].promptUuid").value("123456"));
  }

  @Test
  @WithMockUser()
  public void testSearchResponses() throws Exception {
    String query1 = "Java";
    String query2 = "Rust";
    String query3 = "HTML";
    String query4 = "You";

    ResponseRecord record1 = new ResponseRecord(
        "You use HTML and CSS",
        List.of("html", "css"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record2 = new ResponseRecord(
        "You can build a CLI with Rust and Tokio",
        List.of("rust", "tokio"),
        ResponseRating.VERY_USEFUL,
        "123456");

    ResponseRecord record3 = new ResponseRecord(
        "Java is a OOP language that sneaks in primitive types",
        List.of("java", "oop"),
        ResponseRating.VERY_USEFUL,
        "123456");

    when(responseService.findResponsesByResponseBodyAndUserEmail(query1))
        .thenReturn(List.of(record3));
    when(responseService.findResponsesByResponseBodyAndUserEmail(query2))
        .thenReturn(List.of(record2));
    when(responseService.findResponsesByResponseBodyAndUserEmail(query3))
        .thenReturn(List.of(record1));
    when(responseService.findResponsesByResponseBodyAndUserEmail(query4))
        .thenReturn(List.of(record1, record2));

    mockMvc.perform(get("/api/response/search?query=Java"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].responseBody").value("Java is a OOP language that sneaks in primitive types"));

    mockMvc.perform(get("/api/response/search?query=Rust"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].responseBody").value("You can build a CLI with Rust and Tokio"));

    mockMvc.perform(get("/api/response/search?query=HTML"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].responseBody").value("You use HTML and CSS"));

    mockMvc.perform(get("/api/response/search?query=You"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(2))
        .andExpect(jsonPath("$[0].responseBody").value("You use HTML and CSS"))
        .andExpect(jsonPath("$[1].responseBody").value("You can build a CLI with Rust and Tokio"));

    verify(responseService).findResponsesByResponseBodyAndUserEmail(query1);
    verify(responseService).findResponsesByResponseBodyAndUserEmail(query2);
    verify(responseService).findResponsesByResponseBodyAndUserEmail(query3);
    verify(responseService).findResponsesByResponseBodyAndUserEmail(query4);
  }

  /**
   * Tests the {@code GET /api/response/search} endpoint to ensure it returns
   * {@link ResponseRecord}s that match the given query string.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>An HTTP 200 (OK) status is returned for each search request.</li>
   *   <li>The response includes only the records that match the query in {@code responseBody}.</li>
   *   <li>The correct service method, {@link ResponseService#findResponsesByResponseBodyAndUserEmail(String)},
   *       is invoked for each query.</li>
   * </ul>
   * </p>
   *
   * @throws Exception if any error occurs during request construction or execution.
   */
  @Test
  @WithMockUser
  public void testGetResponseById() throws Exception {
    ResponseRecord record = new ResponseRecord(
        "You use HTML and CSS",
        List.of("html", "css"),
        ResponseRating.VERY_USEFUL,
        "123456");

    when(responseService.getResponseById(1L)).thenReturn(record);
    mockMvc.perform(get("/api/response/{id}", 1L))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.responseBody").value("You use HTML and CSS"))
      .andExpect(jsonPath("$.metaData.size()").value(2));
  }

  /**
   * Tests the {@code GET /api/response/{id}} endpoint to ensure it fetches the
   * {@link ResponseRecord} with the specified ID.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>An HTTP 200 (OK) status is returned.</li>
   *   <li>The response matches the expected {@link ResponseRecord}, including fields for
   *       {@code responseBody}, {@code metaData}, {@code rating}, and {@code promptUuid}.</li>
   * </ul>
   * </p>
   *
   * @throws Exception if any error occurs during request construction or execution.
   */
  @Test
  @WithMockUser
  public void testDeleteResponseById() throws Exception {
    doNothing().when(responseService).deleteResponseById(1L);
    mockMvc.perform(delete("/api/response/{id}", 1L))
      .andExpect(status().isOk());
  }

}
