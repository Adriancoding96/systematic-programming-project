package com.adrain.llm_middleware.util;

import lombok.Getter;
import lombok.Setter;

/**
 * TrieNode represents a single node in a {@link Trie}.
 * <p>
 *     Each node contains an array of child references, a content string,
 *     and a flag indicating if the node corresponds to the end of a word.
 *     The children array is sized at 128 to work with ASCII.
 * </p>
 *
 * @see Trie
 */
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
