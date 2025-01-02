package com.adrain.llm_middleware.record.prompt;

import java.util.List;

public record PromptResponse(String response, List<String> keywords) {}
