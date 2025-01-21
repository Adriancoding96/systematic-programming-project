package com.adrain.llm_middleware.service;

import java.util.List;

import com.adrain.llm_middleware.model.Response;
import com.adrain.llm_middleware.record.response.ResponseRecord;

public interface ResponseService {
  void newResponse(ResponseRecord record);
  List<ResponseRecord> getAllResponsesByUserEmail();
  List<ResponseRecord> findResponsesByResponseBodyAndUserEmail(String responseBody);
  ResponseRecord getResponseById(Long id);
  Response getResponseByPromptId(Long id);
  void deleteResponseById(Long id);
}
