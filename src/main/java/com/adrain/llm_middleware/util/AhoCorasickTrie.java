package com.adrain.llm_middleware.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AhoCorasickTrie extends Trie {

  public AhoCorasickTrie() {
    super();
    this.setRoot(new AhoCorasickNode());
  }

  @Override
  public void insertAll(List<String> words) {
    for(String word : words) {
      insert(word);
    }
  }

  @Override
  public void insert(String word) {
    AhoCorasickNode current = (AhoCorasickNode) getRoot();

    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      int index = ch;

      if(current.getChildren()[index] == null) {
        current.getChildren()[index] = new AhoCorasickNode();
      }
      current = (AhoCorasickNode) current.getChildren()[index];
    }
    current.setEndOfWord(true);
    current.getOutput().add(word);
  }

  public void buildFailureLinks() {
    Queue<AhoCorasickNode> queue = new LinkedList<>();
    AhoCorasickNode root = (AhoCorasickNode) getRoot();

    // Initiate failure links for imediate children of root
    for (int i = 0; i < root.getChildren().length; i++) {
      AhoCorasickNode child = (AhoCorasickNode) root.getChildren()[i];

      if(child != null) {
        child.setFailureLink(root);
        queue.add(child);
      }
    }

    // Build failure links using breadth first search
    while(!queue.isEmpty()) {
      AhoCorasickNode current = queue.poll();

      for (int i = 0; i < current.getChildren().length; i++) {
        AhoCorasickNode child = (AhoCorasickNode) current.getChildren()[i];

        if(child != null) {

          // Find failure link for child
          AhoCorasickNode childFailureLink = current.getFailureLink();
          while(childFailureLink != null && childFailureLink.getChildren()[i] == null) {
            childFailureLink = childFailureLink.getFailureLink();
          }

          if(childFailureLink == null) {
            child.setFailureLink(root);
          } else {
            child.setFailureLink((AhoCorasickNode) childFailureLink.getChildren()[i]);
            child.getOutput().addAll(child.getFailureLink().getOutput());
          }

          queue.add(child);
        }
      }
    }
  }

  public List<String> searchText(String text) {
    AhoCorasickNode current = (AhoCorasickNode) getRoot();
    List<String> result = new ArrayList<>();

    for (int i = 0; i < text.length(); i++) {
      char ch = text.charAt(i);
      int index = ch;

      // Follow failure links
      while(current != null && current.getChildren()[index] == null) {
        current = current.getFailureLink();
      }

      if(current == null) {
        current = (AhoCorasickNode) getRoot();
        continue;
      }

      current = (AhoCorasickNode) current.getChildren()[index];
      if(current.getOutput() != null) {
        result.addAll(current.getOutput());
      }
    }

    return result;
  }
  
}
