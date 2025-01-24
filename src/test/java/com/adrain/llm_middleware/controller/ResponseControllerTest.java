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

  @Test
  @WithMockUser
  public void testDeleteResponseById() throws Exception {
    doNothing().when(responseService).deleteResponseById(1L);
    mockMvc.perform(delete("/api/response/{id}", 1L))
      .andExpect(status().isOk());
  }

}
