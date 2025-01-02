package com.adrain.llm_middleware.util;

import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrieNode {

  private HashMap<Character, TrieNode> children;
  private String content;
  private boolean endOfWord;
  
}
