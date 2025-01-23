package com.adrain.llm_middleware.record.api;

import java.util.List;

/**
 * Represents a response from the OpenAI API for a chat completion request.
 * This record encapsulates the details of the response, including the identifier,
 * object type, creation timestamp, choices, and token usage.
 *
 * @param id      the identifier for the response
 * @param object  the type of object returned, for example "chat completion"
 * @param created the timestamp when the response was created
 * @param choices the list of completion choices generated by the model
 * @param usage   the token usage for the request
 */
public record OpenAiResponse(
    String id,
    String object,
    Long created,
    List<Choice> choices,
    Usage usage
) {
    
    /**
     * Represents a completion choice in the OpenAI response.
     *
     * @param index         the index of the choice in the list
     * @param message       the message generated by the model
     * @param finish_reason the reason why the model stopped generating tokens
     */
    public record Choice(
        int index,
        Message message,
        String finish_reason
    ) {}

    /**
     * Represents a message in the OpenAI response.
     *
     * @param role    the role of the message sender
     * @param content the content of the message
     */
    public record Message(
        String role,
        String content
    ) {}

     /**
     * Represents token usage for the OpenAI request.
     *
     * @param prompt_tokens      the number of tokens used in the prompt
     * @param completion_tokens  the number of tokens used in the completion
     * @param total_tokens       the total number of tokens used
     */
    public record Usage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
    ) {}
}
