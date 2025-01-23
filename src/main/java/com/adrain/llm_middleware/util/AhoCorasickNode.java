package com.adrain.llm_middleware.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * Represents a node in the {@link AhoCorasickTrie} automaton.
 * This class extends {@link TrieNode} extending functionality specific to the
 * AhoCorasick algorithm, such as failure links.
 *
 * <p>Each node contains a failure link ensuring efficient traversal of Trie with
 * a constant time complexity of O(n)+L.</p>
 *
 * @see AhoCorasickTrie
 * @see TrieNode
 */
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
