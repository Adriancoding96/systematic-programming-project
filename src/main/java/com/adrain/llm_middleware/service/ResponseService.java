package com.adrain.llm_middleware.service;

import java.util.List;

import com.adrain.llm_middleware.record.response.ResponseRecord;

public interface ResponseService {
  boolean newResponse(ResponseRecord record);
  List<ResponseRecord> getAllResponsesByUserId();
  ResponseRecord getResponseById(Long id);
  boolean deleteResponseById(Long id);
}
