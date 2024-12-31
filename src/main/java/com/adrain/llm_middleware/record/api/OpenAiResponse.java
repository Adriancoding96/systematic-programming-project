package com.adrain.llm_middleware.record.api;

import java.util.List;

public record OpenAiResponse(
    String id,
    String object,
    Long created,
    List<Choice> choices,
    Usage usage
) {
    public record Choice(
        int index,
        Message message,
        String finish_reason
    ) {}

    public record Message(
        String role,
        String content
    ) {}

    public record Usage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
    ) {}
}
