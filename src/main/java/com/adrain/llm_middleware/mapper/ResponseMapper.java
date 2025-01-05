package com.adrain.llm_middleware.mapper;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResponseMapper {

  ResponseMapper INSTANCE = Mappers.getMapper(ResponseMapper.class);
  ResponseRecord mapResponseToRecord(Response response);
  Response mapRecordToResponse(ResponseRecord record);
  
}
