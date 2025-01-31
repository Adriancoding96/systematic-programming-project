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

/**
 * Unit tests for the {@link PromptMapper} class.
 *
 * <p>This test class verifies the functionality of the {@link PromptMapper}, which is responsible
 * for converting between domain objects such as {@link Prompt} and DTOs like {@link PromptRequest}
 * and {@link PromptRecord}.</p>
 *
 * <p>It uses the Spring testing framework and runs with the {@code test} profile active,
 * ensuring the application configuration is suited for testing scenarios.</p>
 *
 * <p>The tests in this class cover:
 * <ul>
 *   <li>{@link #testToPromptFromRequest()} – Ensures that a {@link PromptRequest} is properly
 *       mapped to a {@link Prompt} domain object.</li>
 *   <li>{@link #testToRecordFromPrompt()} – Ensures that a {@link Prompt} domain object is
 *       properly mapped to a {@link PromptRecord}.</li>
 * </ul>
 * </p>
 *
 * @see PromptMapper
 * @see Prompt
 * @see PromptRequest
 * @see PromptRecord
 */
@SpringBootTest
@ActiveProfiles("test")
public class PromptMapperTest {

  private PromptMapper mapper;

  @BeforeEach
  void setUp() {
    this.mapper = new PromptMapper();
  }

  /**
   * Tests {@link PromptMapper#toPromptFromRequest(PromptRequest)} to ensure that a valid
   * {@link PromptRequest} is correctly mapped to a {@link Prompt} entity.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>The {@link Prompt} object is not {@code null} after mapping.</li>
   *   <li>The {@code prompt} field matches the value in the {@link PromptRequest}.</li>
   * </ul>
   * </p>
   */
  @Test
  public void testToPromptFromRequest() {
    PromptRequest request = new PromptRequest("How do i center a div in html?", "deepseek-v3");
    Prompt prompt = mapper.toPromptFromRequest(request);

    assertNotNull(prompt);
    assertEquals("How do i center a div in html?", prompt.getPrompt());
  }

  /**
   * Tests {@link PromptMapper#toRecordFromPrompt(Prompt)} to ensure that a valid
   * {@link Prompt} entity is correctly mapped to a {@link PromptRecord}.
   *
   * <p>This test verifies the following:
   * <ul>
   *   <li>The {@link PromptRecord} object is not {@code null} after mapping.</li>
   *   <li>The {@code prompt} and {@code uuid} fields match the values from the {@link Prompt}.</li>
   * </ul>
   * </p>
   */
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
