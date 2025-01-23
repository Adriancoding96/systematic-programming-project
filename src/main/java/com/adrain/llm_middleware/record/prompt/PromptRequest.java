package com.adrain.llm_middleware.record.prompt;

import com.adrain.llm_middleware.model.Prompt;
import com.adrain.llm_middleware.model.Response;

/**
 * Represents a request containing details of a {@link Prompt}.
 *
 * @param prompt the text prompt provided by the {@link User}
 * @param model  the identifier of the model to be used for generating the {@link Response}
 *
 * @see Prompt
 * @see User
 * @see Response
 */
public record PromptRequest(String prompt, String model) {}
