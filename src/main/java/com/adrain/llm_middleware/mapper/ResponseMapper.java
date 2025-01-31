package com.adrain.llm_middleware.mapper;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

import org.springframework.stereotype.Component;

/**
 * Component to map {@link ResponseRecord} to {@link Response}
 * && {@link Response} to {@link ResponseRecord}. 
 *
 * @see ResponseRecord
 * @see Response
 * */
@Component
public class ResponseMapper {


  /**
   * Takes a {@link ResponseRecord} and converts it in to a {@link Response}
   *
   * @Param record containing response data.
   * @return response converted from {@link ResponseRecord}
   * */
  public Response toResponse(ResponseRecord record) {
    return new Response(
        null,
        null,
        record.responseBody(),
        record.metaData(),
        record.rating(),
        null);
  }


  /**
   * Takes a {@link Response} and converts it in to a {@link ResponseRecord}. 
   *
   * @Param response model.
   * @return record {@link ResponseRecord} converted from {@link Response}. 
   * */

  public ResponseRecord toRecord(Response response) {
    return new ResponseRecord(
        response.getResponseBody(),
        response.getMetaData(),
        response.getRating(),
        response.getPrompt().getUuid());
  }

}
