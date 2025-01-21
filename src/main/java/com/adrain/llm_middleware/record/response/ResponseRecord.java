package com.adrain.llm_middleware.record.response;

import java.util.List;

import com.adrain.llm_middleware.enums.ResponseRating;

import lombok.Builder;

@Builder
public record ResponseRecord(
    String responseBody,
    List<String> metaData,
    ResponseRating rating,
    String promptUuid
) {}
