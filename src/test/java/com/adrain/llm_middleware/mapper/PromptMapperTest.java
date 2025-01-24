package com.adrain.llm_middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PromptMapperTest {

  private PromptMapper mapper;

  @BeforeEach
  void setUp() {
    this.mapper = new PromptMapper();
  }

  /*
  @Test
  void testMapResponseToRecord() {
    Response response = new Response();
    response.setId(1L);
    response.setPrompt(new Prompt());
    response.setResponseBody("You use css and html LOL");
    response.setMetaData(List.of("css", "html"));
    response.setRating(ResponseRating.VERY_USEFUL);

    ResponseRecord record = mapper.toRecord(response);

    assertNotNull(record);
    assertEquals("You use css and html LOL", record.responseBody());
    assertEquals(List.of("css", "html"), record.metaData());
    assertEquals(ResponseRating.VERY_USEFUL, record.rating());
  }*/

  @Test
  public void testToPromptFromRequest() {
    PromptRequest request = new PromptRequest("How do i center a div in html?", "deepseek-v3");
    Prompt prompt = mapper.toPromptFromRequest(request);

    assertNotNull(prompt);
    assertEquals("How do i center a div in html?", prompt.getPrompt());
  }
  
}
