package com.adrain.llm_middleware.mapper;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

  public Response toResponse(ResponseRecord record) {
    return new Response(
        null,
        null,
        record.responseBody(),
        record.metaData(),
        record.rating());
  }

  public ResponseRecord toRecord(Response response) {
    return new ResponseRecord(
        response.getResponseBody(),
        response.getMetaData(),
        response.getRating());
  }

}
