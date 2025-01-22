package com.adrain.llm_middleware.record.response;

import java.util.List;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ResponseRecord(
    @JsonProperty("responseBody") String responseBody,
    @JsonProperty("metaData") List<String> metaData,
    @JsonProperty("rating") ResponseRating rating,
    @JsonProperty("promptUuid") String promptUuid
) {}
