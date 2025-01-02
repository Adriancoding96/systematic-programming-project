package com.adrain.llm_middleware.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AhoCorasickNode extends TrieNode {
  
  private AhoCorasickNode failureLink;
  private List<String> output;

  public AhoCorasickNode() {
    super();
    this.failureLink = null;
    this.output = new ArrayList<>();
  }

  public AhoCorasickNode(String content) {
    super(content);
    this.failureLink = null;
    this.output = new ArrayList<>();
  }
  
}
