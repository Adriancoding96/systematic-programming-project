package com.adrain.llm_middleware.controller;


import java.util.List;

import com.adrain.llm_middleware.util.AhoCorasickTrie;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  private final AhoCorasickTrie ahoCorasickTrie;

  public TestController() {
    this.ahoCorasickTrie = new AhoCorasickTrie();
    this.ahoCorasickTrie.insert("adrian");
    this.ahoCorasickTrie.insert("java");
    this.ahoCorasickTrie.buildFailureLinks();
  }

  @PostMapping("/search")
  public ResponseEntity<List<String>> searchPatterns(@RequestBody String text) {
    List<String> matches = ahoCorasickTrie.searchText(text);
    return ResponseEntity.ok(matches);
  }
}
