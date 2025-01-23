package com.adrain.llm_middleware.record.response;

import java.util.List;

import com.adrain.llm_middleware.enums.ResponseRating;
import com.adrain.llm_middleware.model.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

/**
 * Represents a {@link Response} containing details of a {@link User} response to a llm response.
 * It is annotated with {@link Builder} to support the builder pattern for object creation.
 * It uses {@code JsonProperty} to help with deserialzation issues.
 *
 * @param responseBody the body of the response
 * @param metaData     the metadata associated with the response
 * @param rating       the {@link ResponseRating} of the response
 * @param promptUuid   the uuid of the prompt associated with this response
 * 
 * @see Builder
 * @see JsonProperty
 * @see ResponseRating
 */
@Builder
public record ResponseRecord(
    @JsonProperty("responseBody") String responseBody,
    @JsonProperty("metaData") List<String> metaData,
    @JsonProperty("rating") ResponseRating rating,
    @JsonProperty("promptUuid") String promptUuid
) {}
