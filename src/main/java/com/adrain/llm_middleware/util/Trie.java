package com.adrain.llm_middleware.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trie {

  private TrieNode root;

  public Trie() {
    root = new TrieNode();
  }

  public void insertAll(List<String> words) {
    for(String word : words) {
      insert(word);
    }
  }

  public void insert(String word) {
    TrieNode current = root;

    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      int index = ch;
      if(current.getChildren()[index] == null) {
        current.getChildren()[index] = new TrieNode();
      }
      current = current.getChildren()[index];
    }
    current.setEndOfWord(true);
  }

  public boolean search(String word) {
    TrieNode current = root;

    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      int index = ch;
      if(current.getChildren()[index] == null) return false;
      current = current.getChildren()[index]; 
    }
    return current != null && current.isEndOfWord();
  }
  
}
