package com.adrain.llm_middleware.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.record.prompt.PromptRecord;
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

  @Test
  public void testToPromptFromRequest() {
    PromptRequest request = new PromptRequest("How do i center a div in html?", "deepseek-v3");
    Prompt prompt = mapper.toPromptFromRequest(request);

    assertNotNull(prompt);
    assertEquals("How do i center a div in html?", prompt.getPrompt());
  }

  @Test
  public void testToRecordFromPrompt() {
    Prompt prompt = new Prompt();
    prompt.setPrompt("How do i center a div in html?");
    prompt.setUuid("123456");

    PromptRecord record = mapper.toRecordFromPrompt(prompt);
    assertNotNull(record);
    assertEquals("How do i center a div in html?", record.prompt());
    assertEquals("123456", record.uuid());
  }
  
}
