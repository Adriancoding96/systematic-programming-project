package com.adrain.llm_middleware.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrieNode {

  TrieNode[] children;
  private String content;
  private boolean isEndOfWord;

  public TrieNode() {
    this.children = new TrieNode[128];
    this.content = "";
    this.isEndOfWord = false;
  }

  public TrieNode(String content) {
    this.children = new TrieNode[128];
    this.content = content;
    this.isEndOfWord = false;
  }
  
}
