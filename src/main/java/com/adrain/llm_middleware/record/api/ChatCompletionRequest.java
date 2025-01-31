package com.adrain.llm_middleware.record.api;

import java.util.List;


/**
 * Represents a request for chat completion in a large language model.
 * This record contains the necessary details for generating a chat completion,
 * including the model to use, the list of messages, the maximum number of tokens,
 * and the temperature for controlling randomness in the response.
 *
 * @param model       the identifier of the model to use for generating the completion
 * @param messages    the list of messages in the conversation
 * @param max_tokens  the maximum number of tokens to generate in the response
 * @param temperature the temperature value to control randomness in response
 */
public record ChatCompletionRequest(
  String model,
  List<Message> messages,
  Integer max_tokens,
  Double temperature
) {}
