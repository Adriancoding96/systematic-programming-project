package com.adrain.llm_middleware.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@Profile("test")
public class PromptMapperTest {

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
  @WithMockUser
  public void testToPromptFromRequest() {
    
  }
  
}
