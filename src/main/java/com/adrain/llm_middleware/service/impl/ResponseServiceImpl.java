package com.adrain.llm_middleware.service.impl;

import java.util.List;

import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.repository.ResponseRepository;

import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl {

  private final ResponseRepository responseRepository;

  public ResponseServiceImpl(ResponseRepository responseRepository) {
    this.responseRepository = responseRepository;
  }

  boolean newResponse(ResponseRecord record) {
    return true;
  }

  List<ResponseRecord> getAllResponsesByUserId() {
    return null;
  }

  ResponseRecord getResponseById(Long id) {
    return null;
  }

  boolean deleteResponseById(Long id) {
    return true;
  }
}
