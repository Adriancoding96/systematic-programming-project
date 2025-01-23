package com.adrain.llm_middleware.record.prompt;

import com.adrain.llm_middleware.model.Prompt;

/**
 * Represents a {@link Prompt} containing data from the database table.
 *
 * @param prompt the body of the prompt
 * @param uuid     the unique identifier for the prompt
 *
 * @see Response
 * @see Prompt
 */
public record PromptRecord(String prompt, String uuid) {
}
