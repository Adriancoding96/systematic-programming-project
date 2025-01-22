package com.adrain.llm_middleware.controller;

import java.util.List;

import com.adrain.llm_middleware.record.response.ResponseRecord;
import com.adrain.llm_middleware.service.ResponseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/response")
public class ResponseController {

  private final ResponseService service;

  @Autowired
  public ResponseController(ResponseService service) {
    this.service = service;
  }

  @PostMapping("/new")
  ResponseEntity<Void> newResponse(@RequestBody ResponseRecord record) {
    service.newResponse(record);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  ResponseEntity<List<ResponseRecord>> getAllResponsesByUserId() {
    List<ResponseRecord> responses = service.getAllResponsesByUserEmail();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/search")
  ResponseEntity<List<ResponseRecord>> searchResponses(@RequestParam String query) {
    List<ResponseRecord> responses = service.findResponsesByResponseBodyAndUserEmail(query);
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/{id}")
  ResponseEntity<ResponseRecord> getResponseById(@PathVariable Long id) {
    ResponseRecord response = service.getResponseById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteResponseById(@PathVariable Long id) {
    service.deleteResponseById(id);
    return ResponseEntity.ok().build();
  }
}
