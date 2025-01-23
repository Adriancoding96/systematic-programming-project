package com.adrain.llm_middleware.record.prompt;

import java.util.List;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;

/**
 * Represents a {@link Response} containing the result of a {@link Prompt}.
 *
 * @param response the generated text
 * @param keywords the list of keywords associated with the response
 * @param uuid     the unique identifier for the response
 *
 * @see Response
 * @see Prompt
 */
public record PromptResponse(String response, List<String> keywords, String uuid) {}
