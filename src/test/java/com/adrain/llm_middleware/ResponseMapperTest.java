package com.adrain.llm_middleware;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.mapper.ResponseMapper;
import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ResponseMapperTest is a test class to verify the correct functionality
 * of the {@link ResponseMapper} class.
 */
@SpringBootTest
public class ResponseMapperTest {

  private ResponseMapper mapper;

  @BeforeEach
  void setUp() {
    this.mapper = new ResponseMapper();
  }

  /**
   * Tests that the {@link ResponseMapper} can map a {@link Response} entity to a
   * {@link ResponseRecord}.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting record is not {@code null}.</li>
   *   <li>The fields match the expected values from the {@code Response} entity.</li>
   * </ul>
   */
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
  }

  /**
   * Tests that the {@link ResponseMapper} can map a {@link ResponseRecord} to a
   * {@link Response} entity.
   * <p>
   *    Verifies that:
   * </p>
   * <ul>
   *   <li>The resulting response is not {@code null}.</li>
   *   <li>The fields match the expected values from the {@code ResponseRecord}.</li>
   * </ul>
   */
  @Test
  void testMapRecordToResponse() {
    ResponseRecord record = new ResponseRecord(
        "You use css and html LOL",
        List.of("css", "html"),
        ResponseRating.VERY_USEFUL
        );

    Response response = mapper.toResponse(record);
    
    assertNotNull(response);
    assertEquals("You use css and html LOL", response.getResponseBody());
    assertEquals(List.of("css", "html"), response.getMetaData());
    assertEquals(ResponseRating.VERY_USEFUL, response.getRating());

  }
  
}
