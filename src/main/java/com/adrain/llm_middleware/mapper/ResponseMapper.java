package com.adrain.llm_middleware.mapper;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {


  /*
   * Takes a record and converts it in to a Response
   * model.
   *
   * @Param record: response record.
   * @return response: Response converted from record
   * */
  public Response toResponse(ResponseRecord record) {
    return new Response(
        null,
        null,
        record.responseBody(),
        record.metaData(),
        record.rating());
  }


  /*
   * Takes a Response and converts it in to a record 
   * model.
   *
   * @Param response: response model.
   * @return record: ResponseRecord converted from response model 
   * */

  public ResponseRecord toRecord(Response response) {
    return new ResponseRecord(
        response.getResponseBody(),
        response.getMetaData(),
        response.getRating());
  }

}
