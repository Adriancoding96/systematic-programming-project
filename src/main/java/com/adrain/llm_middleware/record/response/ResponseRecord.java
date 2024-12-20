package com.adrain.llm_middleware.record.response;

import java.util.List;

public record ResponseRecord(String text, List<String> metaData) {}
